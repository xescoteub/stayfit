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

class StretchesExercises : AppCompatActivity() {
    //create object of listview
    var listView: ListView ?= null
    //create ArrayList of String
    var arrayList: ArrayList<String> = ArrayList()
    //create ArrayList of String
    var exerciseList: ArrayList<Exercise> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stretches_exercises)
        listView = findViewById<View>(R.id.stretches_list) as ListView
        controlListView()
    }
    fun controlListView(){
        //Add elements to arraylist
        var standingHamstringStretch: Exercise = Exercise("Standing Hamstring Stretch", "https://www.youtube.com/watch?v=SAU77ionqJ4", "0", "null", "")
        addExercise(standingHamstringStretch)

        arrayList.add("Piriformis Stretch")
        arrayList.add("Lunge With Spinal Twist")
        arrayList.add("Triceps Stretch")
        arrayList.add("Figure Four Stretch")
        arrayList.add("90/90 Stretch")
        arrayList.add("Frog Stretch")
        arrayList.add("Butterfly Stretch")
        arrayList.add("Seated Shoulder Squeeze")
        arrayList.add("Side Bend Stretch")
        arrayList.add("Lunging Hip Flexor Stretch")
        arrayList.add("Lying Pectoral Stretch")
        arrayList.add("Knee to Chest Stretch")
        arrayList.add("Seated Neck Release")
        arrayList.add("Lying Quad Stretch")
        arrayList.add("Sphinx Pose")
        arrayList.add("Extended Puppy Pose")
        arrayList.add("Pretzel Stretch")
        arrayList.add("Reclining Bound Angle Pose")
        arrayList.add("Standing Quad Stretch")
        arrayList.add("Knees to Chest")
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
