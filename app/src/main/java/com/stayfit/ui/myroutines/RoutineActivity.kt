package com.stayfit.ui.myroutines

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.stayfit.R
import com.stayfit.ui.workouts.exercises.Exercise
import com.stayfit.ui.workouts.exercises.ExerciseActivity
import com.stayfit.ui.workouts.listexercises.*
import com.stayfit.ui.workouts.menu.ArmMenu
import com.stayfit.ui.workouts.menu.ExtraMenu
import java.lang.reflect.Type


class RoutineActivity : AppCompatActivity() {
    //create object of listview
    var listView: ListView?= null
    var arrayList:ArrayList<Exercise> = ArrayList()
    var arrayNames: ArrayList<String> = ArrayList()
    var delayTime:Int = 20
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routine)
        listView = findViewById<ListView>(R.id.routine_list)
        val intent: Intent = this.intent
        val hashMap = intent.getSerializableExtra("routine_map") as HashMap<String, ArrayList<ArrayList<String>>>
        if (hashMap["exercises"]==null){hashMap["exercises"]=ArrayList()}
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

    fun controlListView(){
        //Create Adapter
        val arrayAdapter: ArrayAdapter<*> = ArrayAdapter<Any>(this, android.R.layout.simple_list_item_1, arrayNames as List<Any>?)
        //assign adapter to listview
        listView?.adapter = arrayAdapter
        //add listener to listview
        listView?.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l -> startConcreteExercise(i) }

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
        val editText: EditText = mView.findViewById(R.id.editTextTime)
        timeDialog.setPositiveButton("Apply") { dialog, which ->
            delayTime = editText.text.toString().toInt()
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
}
