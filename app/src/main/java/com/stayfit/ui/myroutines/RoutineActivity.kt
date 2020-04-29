package com.stayfit.ui.myroutines

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.stayfit.MainActivity
import com.stayfit.R
import com.stayfit.ui.workouts.exercises.Exercise
import com.stayfit.ui.workouts.exercises.ExerciseActivity
import java.lang.Exception
import java.lang.reflect.Type


class RoutineActivity : AppCompatActivity() {
    //create object of listview
    var listView: ListView?= null
    var arrayList:ArrayList<Exercise> = ArrayList()
    var arrayNames: ArrayList<String> = ArrayList()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routine)
        listView = findViewById<ListView>(R.id.routine_list)
        val intent: Intent = this.intent
        val hashMap = intent.getSerializableExtra("routine_map") as HashMap<String, ArrayList<ArrayList<String>>>
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

    fun startExercises(view: View) {}

    fun startWorkouts(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
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
        val timeDialog = androidx.appcompat.app.AlertDialog.Builder(this)
        timeDialog.setView(R.layout.routine_change_time)
        timeDialog.setPositiveButton("Apply") { dialog, which ->
            //Actualizar
        }
        timeDialog.setNegativeButton("Cancel") { dialog, which -> }
        timeDialog.show()
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
}
