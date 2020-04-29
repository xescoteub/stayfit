package com.stayfit.ui.workouts.listexercises

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.stayfit.R
import com.stayfit.ui.workouts.exercises.Exercise
import com.stayfit.ui.workouts.exercises.ExerciseActivity
import java.lang.Exception
import java.lang.reflect.Type


class MyExercises : AppCompatActivity() {
    //create object of listview
    var listView: ListView?= null
    //create ArrayList of String
    var arrayList: ArrayList<String> = ArrayList()
    //create ArrayList of String
    var exerciseList: ArrayList<Exercise> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_exercises)
        listView = findViewById<ListView>(R.id.my_exercises_list)
        controlListView()
    }
    fun controlListView(){
        //Add elements to arraylist
        try {
            loadData()
        }catch (e: Exception){

        }
        //Create Adapter
        val arrayAdapter: ArrayAdapter<*> = ArrayAdapter<Any>(this, android.R.layout.simple_list_item_1, arrayList as List<Any>?)
        //assign adapter to listview
        listView?.adapter = arrayAdapter
        //add listener to listview
        listView?.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l -> startConcreteExercise(i) }

    }
    fun startConcreteExercise(i: Int){
        if (i<exerciseList.size){startExercise(exerciseList[i])}
        else{ Toast.makeText(this, "Exercise ${arrayList[i]} will be available coming soon!", Toast.LENGTH_SHORT).show()}
    }
    private fun startExercise(exercise: Exercise) {
        val intent = Intent(this, ExerciseActivity::class.java)
        intent.putExtra("exercise_name",exercise.getParametersList());
        startActivity(intent)
    }
    fun addExercise(exercise: Exercise){
        exerciseList.add(exercise)
        arrayList.add(exercise.getExerciseName())
        saveData()
    }

    fun newExercise(view: View) {
        val intent = Intent(this, FormExercise::class.java)
        startActivityForResult(intent, 2);// Activity is started with requestCode 2
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { // https://developer.android.com/training/basics/intents/result  https://stackoverflow.com/questions/19666572/how-to-call-a-method-in-another-activity-from-activity
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2) {
            if (resultCode == 2){
                var arrayList: ArrayList<String> = data!!.getStringArrayListExtra("LIST")
                addExercise(Exercise( arrayList[0], arrayList[2],"0", "null", arrayList[1]))
                controlListView()
            }
        }
    }
    private fun saveData(){
        var sharedPreferences:SharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)
        var editor: SharedPreferences.Editor = sharedPreferences.edit()
        var gson: Gson = Gson()
        var jsonNames: String = gson.toJson(arrayList)
        var jsonExercise: String = gson.toJson(exerciseList)
        editor.putString("exercise name list", jsonNames)
        editor.putString("exercise list", jsonExercise)
        editor.apply()
    }
    private fun loadData(){
        var sharedPreferences:SharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)
        var gson: Gson = Gson()
        var jsonNames: String? = sharedPreferences.getString("exercise name list", null)
        var jsonExercise: String? = sharedPreferences.getString("exercise list",null)
        val typeName:Type = object : TypeToken<ArrayList<String?>?>() {}.type
        val typeExercise:Type = object : TypeToken<ArrayList<Exercise?>?>() {}.type
        arrayList = gson.fromJson(jsonNames, typeName)
        exerciseList = gson.fromJson(jsonExercise,typeExercise)
        if (arrayList == null) {
            arrayList = ArrayList()
        }
        if (exerciseList == null) {
            exerciseList = ArrayList()
        }
    }
    fun deleteItem(view: View?) {
        listView?.onItemClickListener = OnItemClickListener { a, v, position, id ->
            val adb: AlertDialog.Builder = AlertDialog.Builder(this)
            adb.setTitle("Delete?")
            adb.setMessage("Are you sure you want to delete ${arrayList.get(position)}?")
            adb.setNegativeButton("Cancel", null)
            adb.setPositiveButton(
                "Ok"
            ) { dialog, which ->
                arrayList.removeAt(position)
                exerciseList.removeAt(position)
                saveData()
                controlListView()
            }
            adb.show()
        }
    }

    /*
    private fun saveLists() {
        try {
            val file: File = Environment.getExternalStorageDirectory()
            val filename = File(file, "name_my_exercises")
            val fileExercises = File(file, "my_exercises")
            var fosName = FileOutputStream(filename)
            var fosExercise = FileOutputStream(fileExercises)
            var outName = ObjectOutputStream(fosName)
            var outExercise = ObjectOutputStream(fosExercise)
            outName.writeObject(arrayList)
            outExercise.writeObject(exerciseList)
            outName.close()
            outExercise.close()
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
    }

    private fun readLists() {
        try {
            val file: File = Environment.getExternalStorageDirectory()
            val filename = File(file, "name_my_exercises")
            val fileExercises = File(file, "my_exercises")
            var fisName = FileInputStream(filename)
            var fisExercise = FileInputStream(fileExercises)
            var inName = ObjectInputStream(fisName)
            var inExercise = ObjectInputStream(fisExercise)
            arrayList = inName.readObject() as ArrayList<String>
            exerciseList = inExercise.readObject() as ArrayList<Exercise>
            inName.close()
            inExercise.close()
        } catch (ex: IOException) {
            ex.printStackTrace()
        } catch (ex: ClassNotFoundException) {
            ex.printStackTrace()
        }
    }*/
}

