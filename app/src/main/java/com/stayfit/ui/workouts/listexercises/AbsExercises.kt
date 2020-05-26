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

class AbsExercises : AppCompatActivity() {
    //create object of listview
    var listView: ListView ?= null
    //create ArrayList of String
    var arrayList: ArrayList<String> = ArrayList()
    //create ArrayList of String
    var exerciseList: ArrayList<Exercise> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_abs_exercises)
        listView = findViewById<View>(R.id.abs_list) as ListView
        controlListView()
    }
    fun controlListView(){

        //Add elements to arraylist
        var assistedReverseSideSetup: Exercise = Exercise("Assisted Reverse Side Situp", "https://youtu.be/YEhZrQ_HjZk", "0","null","How to: Start lying on left side, resting most of weight on left hip, with legs in the air at a 45-degree angle, and place left forearm on the floor for support. Bend knees as you bring them toward chest, and lift chest to meet them. Lower back to start. That’s one rep. We recommend to do 15 reps on each side.\n" +
                "\n" +
                "Good for: obliques and transverse abs","null" )
        addExercise(assistedReverseSideSetup)

        var bentLegVUp: Exercise = Exercise("Bent Leg V-Up","https://youtu.be/7X5HLDLswBw", "0", "null","How to: Start lying on back with legs in air and bent at 90-degrees (shins parallel to floor) and hands clasped over chest. In one movement, straighten legs and lift torso up, extending arms and trying to touch toes with hands. Lower back down to start. That’s one rep. We recommend to do 15 reps.\n" +
                "\n" +
                "Good for: six-pack abs and transverse abs","null" )
        addExercise(bentLegVUp)

        addExercise( Exercise("Alternating Toe Reach","https://youtu.be/tzJC-HijGsk", "0", "null","How to: Start lying on back with legs extended in air at 45-degree angle and arms straight out to sides on floor at shoulder level. At the same time, raise right leg up and lift torso trying to touch toes with left hand. Return to start and repeat on the other side. That’s one rep. Do 15 reps.\n" +
                "\n" +
                "Good for: obliques and transverse abs","null" ))
        addExercise( Exercise("Leg Raise and Reach Clap","https://youtu.be/2O469gGYSew", "0", "null","How to: Start lying on back with legs lifted in air at 45-degree angle and arms by side pressing into mat. Without letting lower back lift off floor, raise legs to hip level while curling upper body off floor and bringing hands to clap behind knees. Return to start. That’s one rep. Do 15 reps.\n" +
                "\n" +
                "Good for: six-pack abs and transverse abs","null" ))
        arrayList.add("Lying Windshield Wipers")
        arrayList.add("Russian Twist")
        arrayList.add("Side-To-Side Crunch")
        arrayList.add("Side Plank Dips")
        arrayList.add("Side Plank and Reach Through")
        arrayList.add("Toe Reach")
        arrayList.add("Plank")
        arrayList.add("Plank With Hip Dip")
        arrayList.add("Cross Mountain Climbers")
        arrayList.add("Bicycle Crunch")
        arrayList.add("Deadbug")
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
