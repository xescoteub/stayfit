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

class ShoulderExercises : AppCompatActivity() {
    //create object of listview
    var listView: ListView ?= null
    //create ArrayList of String
    var arrayList: ArrayList<String> = ArrayList()
    //create ArrayList of String
    var exerciseList: ArrayList<Exercise> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shoulder_exercises)
        listView = findViewById<View>(R.id.shoulder_list) as ListView
        controlListView()
    }
    fun controlListView(){
        //Add elements to arraylist
        var barbellOverheadShoulderPress: Exercise = Exercise("Barbell Overhead Shoulder Press","https://www.youtube.com/watch?v=5yWaNOvgFCM", "0", "null", "")
        addExercise(barbellOverheadShoulderPress)

        arrayList.add("Seated Dumbbell Shoulder Press")
        arrayList.add("Front Raise")
        arrayList.add("Reverse Pec Deck Fly")
        arrayList.add("Bent-Over Dumbbell Lateral Raise")
        arrayList.add("Dumbbell Lateral Raise")
        arrayList.add("Push Press")
        arrayList.add("Reverse Cable Crossover")
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
