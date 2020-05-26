package com.stayfit.ui.workouts.listexercises

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


class BicepsExercises : AppCompatActivity() {
    //create object of listview
    var listView: ListView ?= null
    //create ArrayList of String
    var arrayList: ArrayList<String> = ArrayList()
    //create ArrayList of String
    var exerciseList: ArrayList<Exercise> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biceps_exercises)
        listView = findViewById<View>(R.id.biceps_list) as ListView
        controlListView()
    }
    fun controlListView(){
        //Add elements to arraylist
        var barbellCurl: Exercise = Exercise("Barbell Curl","https://www.youtube.com/watch?v=kwG2ipFRgfo", "0", "null", "Dumbbells allow the wrists to move freely, so most people adopt for a slight rotation of the wrist and forearm as they curl, which thickens the muscle group.","null" )
        addExercise(barbellCurl)

        addExercise( Exercise("Chin-Up","https://www.youtube.com/watch?v=T78xCiw_R6g", "0", "null","","null" ))
        //arrayList.add("Chin-Up")
        addExercise( Exercise("Hammer Curl","https://www.youtube.com/watch?v=TwD-YGVP4Bk", "0", "null","The hammer will typically be our strongest curl during a biceps workout. This is because all of our elbow flexors are actively involved, and the forearm and wrist are in a power position. Doing this movement like a concentration curl or preacher curl (on a preacher bench) will minimize cheating and maximize muscle recruitment during the workout.\n" +
                "\n","null" ))
        //arrayList.add("Hammer Curl")
        addExercise( Exercise("Rope Hammer Curl","https://www.youtube.com/watch?v=_tg_GRzZiO4", "0", "null","", "null" ))
        //arrayList.add("Cable Rope Hammer Curl")
        addExercise( Exercise("Incline Dumbbell Curl","https://www.youtube.com/watch?v=b4jOP-spQW8", "0", "null","","null" ))
        //arrayList.add("Incline Dumbbell Curl")
        addExercise( Exercise("Supinated / Reverse Grip Bent Over Row","https://www.youtube.com/watch?v=3gdGSSgDby8", "0", "null","","null" ))
        //arrayList.add("Supinated / Reverse Grip Bent Over Row")
        addExercise( Exercise("Cable Curl","https://www.youtube.com/watch?v=85kXYq7Ssh4", "0", "null","","null" ))
        //arrayList.add("Cable Curl")
        addExercise( Exercise("Concentration Curl","https://www.youtube.com/watch?v=0AUGkch3tzc", "0", "null","Concentration curls place the arm in front of the body with a bent elbow and a rotation in the shoulder. While this decreases recruitment of the long head, it potentially increases biceps thickness and peak by better recruitment of surrounding muscles during your workout.\n" +
                "\n" +
                "With your free hand on your off leg to support your body weight, when you hit failure you can switch over to a hammer grip and burn out a few extra reps.","null" ))
        //arrayList.add("Concentration Curl")
        addExercise( Exercise("EZ-Bar Curl","https://www.youtube.com/watch?v=kwG2ipFRgfo", "0", "null"," It engages both the short and long heads of the biceps muscle and for some people it's a lot more comfortable on the joints and forearms than a straight barbell","null" ))
        //arrayList.add("EZ-Bar Curl")
        addExercise( Exercise("Overhead Cable Curl","https://www.youtube.com/watch?v=5_n3gVeGEqc", "0", "null","This is a great way to practice your front double biceps pose as you train. You can do both cables at once, or alternate between arms","null" ))
        //arrayList.add("Overhead Cable Curl")
        addExercise( Exercise("Decline Dumbbell Curl","https://www.youtube.com/watch?v=OK6vpxXZ2pk&t=10s", "0", "null","","null" ))
        //arrayList.add("Decline Bar Curl")
        addExercise( Exercise("Zottman Curl","https://www.youtube.com/watch?v=ZrpRBgswtHs", "0", "null","In this movement, you hold a dumbbell in each hand and have a palms-up (supinated) grip on the way up and a palms-down (pronated) grip as you lower the weight, so all of your elbow flexors get hit!\n" +
                "\n" +
                "Some of your elbow flexors act as supinators as well, so rotating the wrist and forearm during the curl instead of at the bottom will load up that function.","null" ))
        //arrayList.add("Zottman Curl")

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
