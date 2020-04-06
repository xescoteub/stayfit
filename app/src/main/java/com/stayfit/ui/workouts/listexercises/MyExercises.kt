package com.stayfit.ui.workouts.listexercises

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.stayfit.R
import com.stayfit.ui.workouts.exercises.Exercise
import com.stayfit.ui.workouts.exercises.ExerciseActivity

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
            }
        }
    }
}
