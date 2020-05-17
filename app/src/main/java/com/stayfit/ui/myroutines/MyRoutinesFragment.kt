package com.stayfit.ui.myroutines

import android.app.AlertDialog
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.common.reflect.TypeToken
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.stayfit.R
import com.stayfit.ui.workouts.exercises.Exercise
import kotlinx.android.synthetic.main.fragment_my_routines.*
import java.lang.reflect.Type


class   MyRoutinesFragment: Fragment(){
    lateinit var routinesList: ArrayList<Routine>
    var routineSelected:Routine ?= null
    //var calendarEvents:ActivityFragment ?= null
    var exercisesRoutine: ArrayList<ArrayList<Exercise>> ?= null
    companion object {
        fun newInstance() = MyRoutinesFragment()
    }
    private val TAG = "MyRoutinesFragment"
    private lateinit var viewModel: MyRoutinesViewModel
    private lateinit var mAuth: FirebaseAuth
    var arrayAdapter: ArrayAdapter<ArrayList<Exercise>> ?= null

    // Access a Cloud Firestore instance from your Activity
    val db = FirebaseFirestore.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view:View = inflater.inflate(R.layout.fragment_my_routines, container, false)
        Log.d(TAG, "onCreateView: MyRoutinesFragment created...")

        mAuth = FirebaseAuth.getInstance()

        var button: ImageView = view.findViewById(R.id.ic_deleteRoutine)
        button.setOnClickListener { deleteRoutine() }

        var buttonAdd: ImageView = view.findViewById(R.id.ic_addRoutine)
        buttonAdd.setOnClickListener { newRoutine(view) }

        var buttonCalendar: ImageView = view.findViewById(R.id.ic_addToCalendar)
        buttonCalendar.setOnClickListener { calendarEvent() }

        var buttonDefault: Button = view.findViewById(R.id.btn_default_routines)
        buttonDefault.setOnClickListener { openDefaultRoutines() }

        return view
    }

    private fun calendarEvent() {
        Toast.makeText(activity,"Press the routine that you want to set for a day",Toast.LENGTH_SHORT).show()
        myroutinesRecycler.adapter = RoutineAdapter(routinesList)
        (myroutinesRecycler.adapter as RoutineAdapter).setOnItemClickListener(object :
            RoutineAdapter.ClickListener {
            override fun onItemClick(position: Int, v: View?) {
                val adb: AlertDialog.Builder = AlertDialog.Builder(activity)

                val mView:View = getLayoutInflater().inflate(R.layout.layout_dialog_calendar,null)
                val day: EditText = mView.findViewById(R.id.edit_day)
                val month: EditText = mView.findViewById(R.id.edit_month)
                val year: EditText = mView.findViewById(R.id.edit_year)

                adb.setNegativeButton("Cancel", null)
                adb.setPositiveButton("Ok") { dialog, which ->

                    addEvent(year.text.toString(),month.text.toString() ,day.text.toString(),routinesList.get(position))
                }
                adb.setTitle("Select a day to set the event")
                adb.setView(mView)
                val dialog:AlertDialog =adb.create()
                dialog.show()
            }

            override fun onItemLongClick(position: Int, v: View?) {

            }
        })


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MyRoutinesViewModel::class.java)
        routinesList = ArrayList()
        exercisesRoutine = ArrayList()
        showList()
    }
    private fun showList() {
        try {
            loadDataFireBase()
            loadExercises()
            // loadDataSharedPreferences()
        }catch (e: Exception){
            Log.d(TAG,"Exception: ${e.message}")
        }

        viewModel.setRoutines(getRoutinesNamesList())
        myroutinesRecycler.layoutManager = LinearLayoutManager(activity)
        myroutinesRecycler.addItemDecoration(DividerItemDecoration(activity, 1))
        myroutinesRecycler.adapter = RoutineAdapter(routinesList)
        (myroutinesRecycler.adapter as RoutineAdapter).setOnItemClickListener(object :
            RoutineAdapter.ClickListener {
            override fun onItemClick(position: Int, v: View?) {
                startConcreteRoutine(routinesList[position])
            }
            override fun onItemLongClick(position: Int, v: View?) {
            }
        })
        (myroutinesRecycler.adapter as RoutineAdapter).notifyDataSetChanged()
        Log.d(TAG, "END1")
    }
    fun startConcreteRoutine(r: Routine) {
        try {
            loadExercises() // <--- dóna error extrany HashMap
        }catch (e: Exception){
            Log.d(TAG,"Exception: ${e.message}")
        }
        Log.d(TAG, ">>> startConcreteRoutine: $r")
        val intent = Intent(activity, RoutineActivity::class.java)
        intent.putExtra("routine_map",r.hashMapExercises);
        startActivity(intent)
    }
    private fun saveDataFireBase(r: Routine){

        val currentUserID = mAuth.currentUser?.uid.toString()
        val routine = hashMapOf(
            "name" to r.name,
            "description" to r.description,
            "photo" to r.photo,
            "hashMapExercises" to r.hashMapExercises?.get("exercises")?.let { toArrayListExercise(it) }
        )
        db.collection("routines").get()
            .addOnSuccessListener { result ->
                var userFound = false
                for(user in result){
                    if(user.id.equals(currentUserID)){
                        userFound=true
                    }
                }
                if(userFound){
                    db.collection("routines").document(currentUserID).collection("MyRoutines").document(r.name).set(routine)
                }else{
                    //Adding User credentials
                    val dataUser = HashMap<String, String>()
                    dataUser["user_Auth"] = mAuth.currentUser?.email.toString()
                    db.collection("routines").document(currentUserID).set(dataUser)
                    //Adding User Routine
                    db.collection("routines").document(currentUserID).collection("MyRoutines").document(r.name).set(routine)
                    Log.d(TAG,"Routine added.")
                    Toast.makeText(activity,"Routine ${r.name} added",Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener{ e ->
                Log.w(TAG,"Error getting getting data",e)
            }
    }
    private fun loadDataFireBase(){
        arrayAdapter = activity?.let {
            ArrayAdapter<ArrayList<Exercise>>(
                it,
                android.R.layout.select_dialog_singlechoice
            )
        }
        val user = mAuth.currentUser?.uid.toString()
        db.collection("routines").document(user).collection("MyRoutines")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val routineObj = document.data as HashMap<*, *>
                    val routine = Routine()
                    var h: HashMap<String,ArrayList<ArrayList<String>>> = HashMap()

                    with(routine) {
                        name    = routineObj["name"].toString()
                        description  = routineObj["description"].toString()
                        photo   = routineObj["photo"].toString()
                        hashMapExercises  = h
                        //exercisesRoutine!!.add(routineObj["hashMapExercises"] as ArrayList<Exercise>)
                        arrayAdapter!!.add(routineObj["hashMapExercises"] as ArrayList<Exercise>)
                        Log.d(TAG, "exercisesList: ${routineObj["hashMapExercises"] as ArrayList<Exercise>}")
                        Log.d(TAG, "routine: $routine")
                        routinesList.add(routine)
                    }
                    Log.d(TAG, "routineList: $routinesList")
                }
                Log.d(TAG, "END2")
                myroutinesRecycler.adapter!!.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
    }
    private fun deleteRoutine() {
        Toast.makeText(activity,"Press the routine that you want to delete",Toast.LENGTH_SHORT).show()
        myroutinesRecycler.adapter = RoutineAdapter(routinesList)
        (myroutinesRecycler.adapter as RoutineAdapter).setOnItemClickListener(object :
            RoutineAdapter.ClickListener {
            override fun onItemClick(position: Int, v: View?) {
                val adb: AlertDialog.Builder = AlertDialog.Builder(activity)
                adb.setTitle("Delete?")
                adb.setMessage("Are you sure you want to delete ${routinesList.get(position).name}?")
                adb.setNegativeButton("Cancel", null)
                adb.setPositiveButton("Ok") { dialog, which ->
                    deleteItemFireBase(routinesList.get(position).name)
                    routinesList.removeAt(position)
                    //saveData()
                    // saveDataSharedPreferences()
                    Log.d(TAG, "END3")
                    myroutinesRecycler.adapter!!.notifyDataSetChanged()
                }
                adb.show()
            }
            override fun onItemLongClick(position: Int, v: View?) {

            }
        })

    }
    private fun addRoutine(r: Routine){
        routinesList.add(r)
        saveDataFireBase(r)
        //saveDataSharedPreferences()
        Log.d(TAG, "END2")
        myroutinesRecycler.adapter!!.notifyDataSetChanged()
    }

    private fun newRoutine(view: View) {
        val intent = Intent(activity, FormRoutine::class.java)
        startActivityForResult(intent, 3);// Activity is started with requestCode 2
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { // https://developer.android.com/training/basics/intents/result  https://stackoverflow.com/questions/19666572/how-to-call-a-method-in-another-activity-from-activity
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 3) {
            if (resultCode == 3){
                var arrayList: ArrayList<String> = data!!.getStringArrayListExtra("LIST ROUTINE")
                val list_ex: ArrayList<ArrayList<String>> = ArrayList()
                var parametersExercise: ArrayList<String> = ArrayList()
                parametersExercise.add("funciona?")
                parametersExercise.add("")
                parametersExercise.add("30")
                parametersExercise.add("null")
                parametersExercise.add("")
                list_ex.add(parametersExercise)
                var parametersExercise2: ArrayList<String> = ArrayList()
                parametersExercise2.add("funciona2")
                parametersExercise2.add("")
                parametersExercise2.add("30")
                parametersExercise2.add("null")
                parametersExercise2.add("")
                list_ex.add(parametersExercise2)
                var hashMapExercises: HashMap<String, ArrayList<ArrayList<String>>> = HashMap()
                hashMapExercises.put("exercises",list_ex)
                addRoutine(Routine( arrayList[0], arrayList[1], arrayList[2], hashMapExercises))
            }
        }
    }
    fun findRoutineByName(name: String): Routine{
        var routine:Routine ?= null
        for (r in routinesList){
            if (name.equals(r.name)){
                routine = r
            }
        }
        return routine!!
    }

    fun getRoutinesNamesList(): ArrayList<String>{
        var namesRoutines: ArrayList<String> = ArrayList()
        for (r in routinesList){ namesRoutines.add(r.name)}
        return namesRoutines
    }


    fun addEvent(year:String,month:String,dayOfMonth:String,routine:Routine){

        val dateEvent = "$dayOfMonth-$month-$year"


        val currentUserID = mAuth.currentUser?.uid.toString()

        db.collection("events").get()
            .addOnSuccessListener { result ->
                var userFound = false
                for(user in result){
                    if(user.id.equals(currentUserID)){
                        userFound=true
                    }
                }
                if(userFound){
                    db.collection("events").document(currentUserID).collection("myEvents").get()
                        .addOnSuccessListener { result2 ->
                            var dateEventFound = false
                            for(dayEvent in result2){
                                if(dayEvent.id.equals(dateEvent)){
                                    dateEventFound = true
                                }
                            }
                            if(dateEventFound){
                                db.collection("events").document(currentUserID).collection("myEvents").document(dateEvent).collection("myRoutines").get()
                                    .addOnSuccessListener { result3 ->
                                        var routineAlreadyAdded = false
                                        for(routinesR in result3){
                                            if(routinesR.id.equals(routine.name)){
                                                routineAlreadyAdded = true
                                            }
                                        }
                                        if(routineAlreadyAdded){
                                            Log.d(TAG,"Event already added.")
                                            Toast.makeText(activity,"Event ${routine.name} already added",Toast.LENGTH_SHORT).show()
                                        }else{
                                            //Adding User Event Routine
                                            val routineData = hashMapOf(
                                                "name" to routine.name,
                                                "description" to routine.description,
                                                "photo" to routine.photo,
                                                "hashMapExercises" to routine.hashMapExercises?.get("exercises")?.let { toArrayListExercise(it) }
                                            )
                                            db.collection("events").document(currentUserID).collection("myEvents").document(dateEvent).collection("myRoutines").document(routine.name).set(routineData)
                                            Log.d(TAG,"Event added.")
                                            Toast.makeText(activity,"Event ${routine.name} added",Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    .addOnFailureListener{ e3 ->
                                        Log.w(TAG,"Error getting getting data",e3)
                                    }
                            }else{
                                //Adding User Event day
                                val dataEvent = HashMap<String, String>()
                                dataEvent["event_Day"] = dateEvent
                                db.collection("events").document(currentUserID).collection("myEvents").document(dateEvent).set(dataEvent)
                                //Adding User Event Routine
                                val routineData = hashMapOf(
                                    "name" to routine.name,
                                    "description" to routine.description,
                                    "photo" to routine.photo,
                                    "hashMapExercises" to routine.hashMapExercises?.get("exercises")?.let { toArrayListExercise(it) }
                                )
                                db.collection("events").document(currentUserID).collection("myEvents").document(dateEvent).collection("myRoutines").document(routine.name).set(routineData)
                                Log.d(TAG,"Event added.")
                                Toast.makeText(activity,"Event ${routine.name} added",Toast.LENGTH_SHORT).show()
                            }
                        }
                        .addOnFailureListener{ e2 ->
                            Log.w(TAG,"Error getting getting data",e2)
                        }
                }else{
                    //Adding User credentials
                    val dataUser = HashMap<String, String>()
                    dataUser["user_Auth"] = mAuth.currentUser?.email.toString()
                    db.collection("events").document(currentUserID).set(dataUser)
                    //Adding User Event day
                    val dataEvent = HashMap<String, String>()
                    dataEvent["event_Day"] = dateEvent
                    db.collection("events").document(currentUserID).collection("myEvents").document(dateEvent).set(dataEvent)
                    //Adding User Event Routine
                    val routineData = hashMapOf(
                        "name" to routine.name,
                        "description" to routine.description,
                        "photo" to routine.photo,
                        "hashMapExercises" to routine.hashMapExercises?.get("exercises")?.let { toArrayListExercise(it) }
                    )
                    db.collection("events").document(currentUserID).collection("myEvents").document(dateEvent).collection("myRoutines").document(routine.name).set(routineData)
                    Log.d(TAG,"Event added.")
                    Toast.makeText(activity,"Event ${routine.name} added",Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener{ e ->
                Log.w(TAG,"Error getting getting data",e)
            }
    }
    fun toArrayListExercise(exercises: ArrayList<ArrayList<String>>): ArrayList<Exercise>{
        var arrayList:ArrayList<Exercise> = ArrayList()
        for (parametersExercise in exercises) {
            arrayList.add(Exercise(parametersExercise[0],parametersExercise[1],parametersExercise[2],parametersExercise[3],parametersExercise[4]))
        }
        return arrayList
    }
    /*
    // TOOD : return type --> ArrayList<ArrayList<String>>
    fun arrayListToHashMap(exercises: ArrayList<Exercise>) {
        Log.d(TAG, "arrayListToHashMap $exercises")
        var arrayList:ArrayList<ArrayList<String>> = ArrayList()
        for (ex in exercises.indices) {
            Log.d(TAG, ">>> $ex")
            //arrayList.add(ex.getParametersList())
        }
//        return arrayList
    }
    */
    private fun arrayListExerciseToArrayListStrings(exercises: ArrayList<Exercise>): ArrayList<ArrayList<String>>{
        var arrayList:ArrayList<ArrayList<String>> = ArrayList()
        Log.d(TAG, "Class ${exercises.javaClass}")
        Log.d(TAG, "EX ${exercises}")
        for (ex in exercises) {
            arrayList.add(ex.getParametersList())
        }
        return arrayList
    }
    private fun loadExercises(){
        for (r in routinesList.indices) {
            Log.d(TAG, "load exercisesList: ${arrayAdapter!!.getItem(r)}")
            var h: HashMap<String,ArrayList<ArrayList<String>>> = HashMap()
            h["exercises"] = arrayListExerciseToArrayListStrings(arrayAdapter!!.getItem(r)!!)
            routinesList[r].hashMapExercises = h
        }
    }
    private fun loadDataSharedPreferences() {
        var sharedPreferences: SharedPreferences =
            this.requireActivity().getSharedPreferences("shared preferences", MODE_PRIVATE)
        var gson: Gson = Gson()
        var jsonRoutines: String? = sharedPreferences.getString("routines list", null)
        val typeRoutine: Type = object : TypeToken<ArrayList<Routine?>?>() {}.type
        routinesList = gson.fromJson(jsonRoutines, typeRoutine)
        if (routinesList == null) {
            routinesList = ArrayList()
        }
    }

    private fun saveDataSharedPreferences() {
        var sharedPreferences: SharedPreferences = this.requireActivity().getSharedPreferences("shared preferences", MODE_PRIVATE)
        var editor: SharedPreferences.Editor = sharedPreferences.edit()
        var gson: Gson = Gson()
        var jsonRoutines: String = gson.toJson(routinesList)
        editor.putString("routines list", jsonRoutines)
        editor.apply()
    }
    fun deleteItemFireBase(exercise_name: String) {
        val currentUserID = mAuth.currentUser?.uid.toString()
        db.collection("routines").document(currentUserID).collection("MyRoutines").document(exercise_name)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }

    private fun saveDataPhoto(r: Routine){

        val currentUserID = mAuth.currentUser?.uid.toString()
        val routine = hashMapOf(
            "name" to r.name,
            "description" to r.description,
            "photo" to r.photo,
            "hashMapExercises" to r.hashMapExercises?.get("exercises")?.let { toArrayListExercise(it) }
        )
        db.collection("routines").get()
            .addOnSuccessListener { result ->
                var userFound = false
                for(user in result){
                    if(user.id.equals(currentUserID)){
                        userFound=true
                    }
                }
                if(userFound){
                    db.collection("routines").document(currentUserID).collection("MyRoutines").document(r.name).set(routine)
                }else{
                    //Adding User credentials
                    val dataUser = HashMap<String, String>()
                    dataUser["user_Auth"] = mAuth.currentUser?.email.toString()
                    db.collection("routines").document(currentUserID).set(dataUser)
                    //Adding User Routine
                    db.collection("routines").document(currentUserID).collection("MyRoutines").document(r.name).set(routine)
                    Log.d(TAG,"Routine added.")
                    Toast.makeText(activity,"Routine ${r.name} added",Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener{ e ->
                Log.w(TAG,"Error getting getting data",e)
            }
    }
    private fun loadDataPhoto(){
        val user = mAuth.currentUser?.uid.toString()
        db.collection("routines").document(user).collection("MyRoutines")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val routineObj = document.data as HashMap<*, *>
                    val routine = Routine()
                    var h: HashMap<String,ArrayList<ArrayList<String>>> = HashMap()

                    with(routine) {
                        name    = routineObj["name"].toString()
                        description  = routineObj["description"].toString()
                        photo   = routineObj["photo"].toString()
                        hashMapExercises  = h
                        exercisesRoutine!!.add(routineObj["hashMapExercises"] as ArrayList<Exercise>)
                        Log.d(TAG, "exercisesList: ${routineObj["hashMapExercises"] as ArrayList<Exercise>}")
                        Log.d(TAG, "routine: $routine")
                        routinesList.add(routine)
                    }
                    Log.d(TAG, "routineList: $routinesList")
                }
                Log.d(TAG, "END2")
                myroutinesRecycler.adapter!!.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }

    }
    fun openDefaultRoutines(){
        val intent = Intent(activity, DefaultRoutinesActivity::class.java)
        startActivity(intent)
    }
}