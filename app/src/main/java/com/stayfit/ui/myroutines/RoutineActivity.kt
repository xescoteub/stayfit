package com.stayfit.ui.myroutines

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.stayfit.R
import com.stayfit.ui.workouts.exercises.Exercise
import com.stayfit.ui.workouts.exercises.ExerciseActivity
import com.stayfit.ui.workouts.listexercises.*
import com.stayfit.ui.workouts.menu.ArmMenu
import com.stayfit.ui.workouts.menu.ExtraMenu
import kotlinx.android.synthetic.main.fragment_my_routines.*
import java.lang.Exception
import java.lang.reflect.Type


class RoutineActivity : AppCompatActivity() {
    //create object of listview
    var listView: ListView?= null
    var arrayList:ArrayList<Exercise> = ArrayList()
    var arrayNames: ArrayList<String> = ArrayList()
    var delayTime:Int = 20
    var nameR: String ?=null
    private lateinit var mAuth: FirebaseAuth
    val db = FirebaseFirestore.getInstance()
    var routine:Routine ?=null
    private val TAG = "RoutineActivity"
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routine)
        listView = findViewById<ListView>(R.id.routine_list)
        val intent: Intent = this.intent
        mAuth = FirebaseAuth.getInstance()
        val hashMap = intent.getSerializableExtra("routine_map") as HashMap<String, ArrayList<ArrayList<String>>>
        if (hashMap["exercises"]==null){hashMap["exercises"]=ArrayList()}
        nameR = intent.getStringExtra("routine_name") as String
        if (nameR.equals("")) {
            val delete_btn = findViewById<ImageView>(R.id.ic_deleteExercise)
            delete_btn.visibility = View.GONE
            val add_btn = findViewById<ImageView>(R.id.ic_addExercise)
            add_btn.visibility = View.GONE
            val set_time_btn = findViewById<ImageView>(R.id.ic_setTimeBetweenExercises)
            set_time_btn.visibility = View.GONE
            val param = listView!!.layoutParams as ViewGroup.MarginLayoutParams
            param.setMargins(0,100,0,0)
            listView!!.layoutParams = param
        }
        managerParametersIntent(hashMap["exercises"]!!)
        controlListView()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun managerParametersIntent(exercises: ArrayList<ArrayList<String>>){
        if (exercises.size>0){
        for (parametersExercise in exercises) {
            arrayList.add(Exercise(parametersExercise[0],parametersExercise[1],parametersExercise[2],parametersExercise[3],parametersExercise[4]))
            arrayNames.add(parametersExercise[0])
        }}
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun controlListView(){
        try {
            loadDataFireBase()
        }catch (e: Exception){

        }
        //Create Adapter
        val arrayAdapter: ArrayAdapter<*> = ArrayAdapter<Any>(this, android.R.layout.simple_list_item_1, arrayNames as List<Any>?)
        //assign adapter to listview
        listView?.adapter = arrayAdapter
        //add listener to listview
        listView?.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l -> startConcreteExercise(i) }
        (listView?.adapter as ArrayAdapter<*>).notifyDataSetChanged()

    }
    fun startConcreteExercise(i: Int){
        if (i<arrayList.size){startExercise(arrayList[i])}
        else{ Toast.makeText(this, "Exercise ${arrayNames[i]} will be available coming soon!", Toast.LENGTH_SHORT).show()}
    }
    private fun startExercise(exercise: Exercise) {
        val intent = Intent(this, ExerciseActivity::class.java)
        intent.putExtra("exercise_name",exercise.getParametersList());
        //Toast.makeText(this, "Exercise ${exercise.getExerciseName()} will be available coming soon!", Toast.LENGTH_SHORT).show()
        startActivity(intent)
    }

    fun startExercises(view: View) {
        for (exercise in arrayList.reversed()){
            val intent = Intent(this, ExerciseActivity::class.java)
            intent.putExtra("exercise_name",exercise.getParametersList());
            startActivity(intent)
            val intentdelay = Intent(this, RestTimeActivity::class.java)
            intentdelay.putExtra("delay_time",delayTime);
            startActivity(intentdelay)
        }
        //Toast.makeText(this, "Congratulations!", Toast.LENGTH_SHORT).show()
    }

    fun startWorkouts(view: View) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Choose a category")
        val categories = arrayOf("ARM","LEG","ABS","CHEST","BACK","EXTRA","MY EXERCISES")
        builder.setItems(categories) { dialog, which ->
            when (which) {
                0 -> { startArmMenu() }
                1 -> { startLegExercises() }
                2 -> { startAbsExercises() }
                3 -> {startChestExercises() }
                4 -> { startBackExercises() }
                5 -> {startExtraMenu() }
                6 -> { startMyExercisesMenu() }
            }
        }
        // create and show the alert dialog
        val dialog = builder.create()
        dialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun deleteItem(view: View) {
        listView?.onItemClickListener = AdapterView.OnItemClickListener { a, v, position, id ->
            val adb: AlertDialog.Builder = AlertDialog.Builder(this)
            adb.setTitle("Delete?")
            adb.setMessage("Are you sure you want to delete ${arrayNames.get(position)}?")
            adb.setNegativeButton("Cancel", null)
            adb.setPositiveButton(
                "Ok"
            ) { dialog, which ->
                arrayList.removeAt(position)
                arrayNames.removeAt(position)
                saveData()
                controlListView()
            }
            adb.show()
        }
    }
    fun setTimeBetweenExercises(view: View) {
        val timeDialog = AlertDialog.Builder(this)
        //timeDialog.setView(R.layout.routine_change_time)
        val mView:View = getLayoutInflater().inflate(R.layout.routine_change_time,null)
        val secTxt: TextView = mView.findViewById(R.id.ActualTime)
        secTxt.text = delayTime.toString() + " seconds"
        val editText: EditText = mView.findViewById(R.id.editTextTime)
        timeDialog.setPositiveButton("Apply") { dialog, which ->
            delayTime = editText.text.toString().toInt()
            saveDataFireBase(routine!!)
            //Toast.makeText(this, "$delayTime", Toast.LENGTH_SHORT).show()
        }
        timeDialog.setNegativeButton("Cancel") { dialog, which -> }
        timeDialog.setView(mView)
        val dialog:AlertDialog =timeDialog.create()
        dialog.show()
    }
    private fun saveData(){
        var sharedPreferences: SharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)
        var editor: SharedPreferences.Editor = sharedPreferences.edit()
        var gson: Gson = Gson()
        var jsonNames: String = gson.toJson(arrayNames)
        var jsonExercise: String = gson.toJson(arrayList)
        editor.putString("exercise name list", jsonNames)
        editor.putString("exercise list", jsonExercise)
        editor.apply()
    }
    private fun loadData(){
        var sharedPreferences: SharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)
        var gson: Gson = Gson()
        var jsonNames: String? = sharedPreferences.getString("exercise name list", null)
        var jsonExercise: String? = sharedPreferences.getString("exercise list",null)
        val typeName: Type = object : TypeToken<ArrayList<String?>?>() {}.type
        val typeExercise: Type = object : TypeToken<ArrayList<Exercise?>?>() {}.type
        arrayNames = gson.fromJson(jsonNames, typeName)
        arrayList = gson.fromJson(jsonExercise,typeExercise)
        if (arrayList == null) {
            arrayList = ArrayList()
        }
        if (arrayNames == null) {
            arrayNames = ArrayList()
        }
    }

    fun startArmMenu() {
        val intent = Intent(this, ArmMenu::class.java)
        startActivity(intent)
    }
    fun startLegExercises() {
        val intent = Intent(this, LegExercises::class.java)
        startActivity(intent)
    }
    fun startAbsExercises() {
        val intent = Intent(this, AbsExercises::class.java)
        startActivity(intent)
    }
    fun startChestExercises() {
        val intent = Intent(this, ChestExercises::class.java)
        startActivity(intent)
    }
    fun startBackExercises() {
        val intent = Intent(this, BackExercises::class.java)
        startActivity(intent)
    }
    fun startExtraMenu() {
        val intent = Intent(this, ExtraMenu::class.java)
        startActivity(intent)
    }
    fun startMyExercisesMenu() {
        val intent = Intent(this, MyExercises::class.java)
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadDataFireBase(){
        val user = mAuth.currentUser?.uid.toString()
        db.collection("routines").document(user).collection("MyRoutines").document(nameR!!)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val routineObj = document.data as HashMap<*, *>
                    val r = Routine()
                    var h: HashMap<String,ArrayList<ArrayList<String>>> = HashMap()

                    with(r) {
                        h["exercises"] = ArrayList()
                        name    = routineObj["name"].toString()
                        description  = routineObj["description"].toString()
                        photo   = routineObj["photo"].toString()
                        hashMapExercises  = h
                        // exercisesRoutine!!.add(routineObj["hashMapExercises"] as ArrayList<Exercise>)
                        //arrayList = routineObj["hashMapExercises"] as ArrayList<Exercise>
                        Log.d(TAG, "exercisesList: ${routineObj["hashMapExercises"] as ArrayList<Exercise>}")
                        Log.d(TAG, "routine: $r")
                        if (routineObj["time_be"].toString().equals(""))
                            delayTime = 20
                        else
                            delayTime = routineObj["time_be"].toString().toInt()
                        routine = r
                    }

                } else {
                    Log.d(TAG, "No such document")
                }
            }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ", exception)
                }
    }
    private fun saveDataFireBase(r: Routine){

        val currentUserID = mAuth.currentUser?.uid.toString()
        val routine = hashMapOf(
            "name" to r.name,
            "description" to r.description,
            "photo" to r.photo,
            "hashMapExercises" to r.hashMapExercises?.get("exercises")?.let { toArrayListExercise(it) },
            "time_be" to delayTime.toString()
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
                    Toast.makeText(this,"Routine ${r.name} added",Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener{ e ->
                Log.w(TAG,"Error getting getting data",e)
            }
    }
    fun getArrayNames(ex: ArrayList<Exercise>): ArrayList<String>{
        var a: ArrayList<String> = ArrayList()
        for (e in ex)
            a.add(e.getExerciseName())
        return a
    }
    fun toArrayListExercise(exercises: ArrayList<ArrayList<String>>): ArrayList<Exercise>{
        var arrayList:ArrayList<Exercise> = ArrayList()
        for (parametersExercise in exercises) {
            arrayList.add(Exercise(parametersExercise[0],parametersExercise[1],parametersExercise[2],parametersExercise[3],parametersExercise[4]))
        }
        return arrayList
    }
    private fun arrayListExerciseToArrayListStrings(exercises: ArrayList<Exercise>): ArrayList<ArrayList<String>>{
        var arrayList:ArrayList<ArrayList<String>> = ArrayList()
        Log.d(TAG, "Class ${exercises.javaClass}")
        Log.d(TAG, "EX ${exercises}")
        for (ex in exercises) {
            arrayList.add(ex.getParametersList())
        }
        return arrayList
    }

}
