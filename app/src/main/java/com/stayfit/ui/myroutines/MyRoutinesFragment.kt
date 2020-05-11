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
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.stayfit.R
import com.stayfit.ui.activity.ActivityFragment
import kotlinx.android.synthetic.main.fragment_my_routines.*
import java.lang.reflect.Type


class   MyRoutinesFragment: Fragment(){
    lateinit var routinesList: ArrayList<Routine>
    var routineSelected:Routine ?= null
    //var calendarEvents:ActivityFragment ?= null

    companion object {
        fun newInstance() = MyRoutinesFragment()
    }
    private val TAG = "MyRoutinesFragment"
    private lateinit var viewModel: MyRoutinesViewModel
    private lateinit var mAuth: FirebaseAuth

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

        return view
    }

    private fun calendarEvent() {
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

    /*
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var button_delete: ImageButton = getView()!!.findViewById(R.id.ic_deleteRoutine)
    } */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MyRoutinesViewModel::class.java)
        routinesList = ArrayList()
        showList()
    }
    private fun showList() {
        try {
            loadData()
        }catch (e: Exception){
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
    }

    fun startConcreteRoutine(r: Routine){
        val intent = Intent(activity, RoutineActivity::class.java)
        intent.putExtra("routine_map",r.hashMapExercises);
        startActivity(intent)
    }
    private fun saveData(r: Routine){
        /*
        var sharedPreferences: SharedPreferences = this.requireActivity().getSharedPreferences("shared preferences", MODE_PRIVATE)
        var editor: SharedPreferences.Editor = sharedPreferences.edit()
        var gson: Gson = Gson()
        var jsonRoutines: String = gson.toJson(routinesList)
        editor.putString("routines list", jsonRoutines)
        editor.apply()*/
        val currentUserID = mAuth.currentUser?.uid.toString()

        db.collection("routines").get()
            .addOnSuccessListener { result ->
                var userFound = false
                for(user in result){
                    if(user.id.equals(currentUserID)){
                        userFound=true
                    }
                }
                if(userFound){
                    db.collection("routines").document(currentUserID).collection("MyRoutines").document(r.name).set(r)
                }else{
                    //Adding User credentials
                    val dataUser = HashMap<String, String>()
                    dataUser["user_Auth"] = mAuth.currentUser?.email.toString()
                    db.collection("routines").document(currentUserID).set(dataUser)
                    //Adding User Routine
                    db.collection("routines").document(currentUserID).collection("MyRoutines").document(r.name).set(r)
                    Log.d(TAG,"Routine added.")
                    Toast.makeText(activity,"Event ${r.name} added",Toast.LENGTH_SHORT).show()
                }
    }
    .addOnFailureListener{ e ->
        Log.w(TAG,"Error getting getting data",e)
    }
    }
    private fun loadData(){
        /*
        var sharedPreferences: SharedPreferences = this.requireActivity().getSharedPreferences("shared preferences", MODE_PRIVATE)
        var gson: Gson = Gson()
        var jsonRoutines: String? = sharedPreferences.getString("routines list", null)
        val typeRoutine: Type = object : TypeToken<ArrayList<Routine?>?>() {}.type
        routinesList = gson.fromJson(jsonRoutines,typeRoutine)
        if (routinesList == null) {
            routinesList = ArrayList()
        } */
        val user = mAuth.currentUser?.uid.toString()
        db.collection("routines").document(user).collection("MyRoutines")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    routinesList.add(document.toObject(Routine::class.java))
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }

    }
    private fun deleteRoutine() {
        myroutinesRecycler.adapter = RoutineAdapter(routinesList)
        (myroutinesRecycler.adapter as RoutineAdapter).setOnItemClickListener(object :
            RoutineAdapter.ClickListener {
            override fun onItemClick(position: Int, v: View?) {
                val adb: AlertDialog.Builder = AlertDialog.Builder(activity)
                adb.setTitle("Delete?")
                adb.setMessage("Are you sure you want to delete ${routinesList.get(position).name}?")
                adb.setNegativeButton("Cancel", null)
                adb.setPositiveButton("Ok") { dialog, which ->
                    routinesList.removeAt(position)
                    //saveData()
                    showList()
                }
                adb.show()
            }
            override fun onItemLongClick(position: Int, v: View?) {

            }
        })
    }
    private fun addRoutine(r: Routine){
        routinesList.add(r)
        saveData(r)
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
                var hashMapExercises: HashMap<String, ArrayList<ArrayList<String>>> = HashMap()
                hashMapExercises.put("exercises",list_ex)
                addRoutine(Routine( arrayList[0], arrayList[1], arrayList[2], hashMapExercises))
                showList()
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
                                            val dataRoutine = HashMap<String, String>()
                                            dataRoutine["routine"] = routine.name //Ha de ser un object
                                            db.collection("events").document(currentUserID).collection("myEvents").document(dateEvent).collection("myRoutines").document(routine.name).set(dataRoutine)
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
                                val dataRoutine = HashMap<String, String>()
                                dataRoutine["routine"] = routine.name //Ha de ser un object
                                db.collection("events").document(currentUserID).collection("myEvents").document(dateEvent).collection("myRoutines").document(routine.name).set(dataRoutine)
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
                    val dataRoutine = HashMap<String, String>()
                    dataRoutine["routine"] = routine.name //Ha de ser un object
                    db.collection("events").document(currentUserID).collection("myEvents").document(dateEvent).collection("myRoutines").document(routine.name).set(dataRoutine)
                    Log.d(TAG,"Event added.")
                    Toast.makeText(activity,"Event ${routine.name} added",Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener{ e ->
                Log.w(TAG,"Error getting getting data",e)
            }
    }
}