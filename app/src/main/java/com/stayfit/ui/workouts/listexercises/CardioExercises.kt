package com.stayfit.ui.workouts.listexercises

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.stayfit.R
import com.stayfit.ui.workouts.exercises.Exercise
import com.stayfit.ui.workouts.exercises.ExerciseActivity

class CardioExercises : AppCompatActivity() {
    //create object of listview
    var listView: ListView ?= null
    //create ArrayList of String
    var arrayList: ArrayList<String> = ArrayList()
    //create ArrayList of String
    var exerciseList: ArrayList<Exercise> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cardio_exercises)
        listView = findViewById<View>(R.id.cardio_list) as ListView
        controlListView()
    }
    fun controlListView(){
        var runningstairs: Exercise = Exercise("Running stairs", "https://www.youtube.com/watch?v=BvskUKjNAH8", "0", "null", "")
        addExercise(runningstairs)

        arrayList.add("Burpees")
        arrayList.add("Jump rope double-unders")
        arrayList.add("Jump lunges")
        arrayList.add("Mountain climbers")
        arrayList.add("Jump squats")
        arrayList.add("Speed skater lunge")
        arrayList.add("Crab plank")
        arrayList.add("Bicycle crunches")
        arrayList.add("Jumping jacks")
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
    private fun addExercise(exercise: Exercise){
        exerciseList.add(exercise)
        arrayList.add(exercise.getExerciseName())
    }
}
