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

class LegExercises : AppCompatActivity() {
    //create object of listview
    var listView: ListView ?= null
    //create ArrayList of String
    var arrayList: ArrayList<String> = ArrayList()
    //create ArrayList of String
    var exerciseList: ArrayList<Exercise> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leg_exercises)
        listView = findViewById<View>(R.id.leg_list) as ListView
        controlListView()
    }
    fun controlListView(){
        //Add elements to arraylist
        var standingForwardBend: Exercise = Exercise("Standing Forward Bend","https://www.youtube.com/watch?v=2jvX4_vc_u0", "0", "null", "")
        addExercise(standingForwardBend)

        addExercise( Exercise("Kneeling Hip Flexor Stretch","https://www.youtube.com/watch?v=kyWMLob5SGk", "0", "null",""))
        //arrayList.add("Kneeling Hip Flexor Stretch")
        addExercise( Exercise("Leg Press","https://www.youtube.com/watch?v=Aq5uxXrXq7c", "0", "null",""))
        //arrayList.add("Leg Press")
        addExercise( Exercise("Barbell Squats","https://www.youtube.com/watch?v=1oed-UmAxFs", "0", "null",""))
        //arrayList.add("Barbell Squats")
        addExercise( Exercise("Jump Squat","https://www.youtube.com/watch?v=J6Y520KkwOA", "0", "null",""))
        //arrayList.add("Jump Squat")
        addExercise( Exercise("Hack Squat","https://www.youtube.com/watch?v=0tn5K9NlCfo", "0", "null",""))
        //arrayList.add("Hack Squat")
        addExercise( Exercise("Romanian Deadlift","https://www.youtube.com/watch?v=2SHsk9AzdjA", "0", "null","How:\n" +
                "\n" +
                "Stand behind a grounded barbell. Bend your knees slightly to grab it, keeping your shins, back and hips straight.\n" +
                "Without bending your back, push your hips forwards to lift the bar. From upright, push your hips back to lower the bar, bending your knees only slightly.\n" +
                "Why: “It’s a great variation on the traditional deadlift that focuses on your glutes and superior end of your hamstrings,” says Leonard. Practising hip flexion exercises – like the Romanian deadlift – can seriously improve your sprint speed and agility. After eight weeks of hip flexor-specific training, participants in a University of Florida study saw their sprint and shuttle run PBs improve by around four per cent and nine per cent respectively."))
        //arrayList.add("Romanian Deadlift")
        addExercise( Exercise("Standing Calf Raises","https://www.youtube.com/watch?v=-M4-G8p8fmc", "0", "null",""))
        //arrayList.add("Standing Calf Raises")
        addExercise( Exercise("Leg Extensions","https://www.youtube.com/watch?v=YyvSfVjQeL0", "0", "null",""))
        //arrayList.add("Leg Extensions")
        addExercise( Exercise("Lying Leg Curls","https://www.youtube.com/watch?v=LxI-ec8cQYo", "0", "null",""))
        //arrayList.add("Lying Leg Curls")
        addExercise( Exercise("Dumbbell Step Ups","https://www.youtube.com/watch?v=S24Do-rZncI", "0", "null",""))
        //arrayList.add("Dumbbell Step Ups")
        addExercise( Exercise("Reverse Lunge","https://www.youtube.com/watch?v=_ggxoUsCx7A", "0", "null",""))
        //arrayList.add("Reverse Lunge")
        addExercise( Exercise("Walking Lunge","https://www.youtube.com/watch?v=L8fvypPrzzs", "0", "null",""))
        //arrayList.add("Walking Lunge")
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
