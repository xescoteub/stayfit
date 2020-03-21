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
import com.stayfit.ui.workouts.exercises.abs.AssistedReverseSideSetup

class AbsExercises : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_abs_exercises)
        controlListView()
    }
    fun controlListView(){
        //create object of listview
        var listView: ListView = findViewById<View>(R.id.abs_list) as ListView
        //create ArrayList of String
        var arrayList: ArrayList<String> = ArrayList()
        //Add elements to arraylist
        arrayList.add("Assisted Reverse Side Situp")
        arrayList.add("Bent Leg V-Up")
        arrayList.add("Alternating Toe Reach")
        arrayList.add("Leg Raise and Reach Clap")
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
        listView.setAdapter(arrayAdapter)
        //add listener to listview
        listView.setOnItemClickListener(AdapterView.OnItemClickListener { adapterView, view, i, l -> startConcreteExercise(arrayList[i].toString()) })
    }

    fun startConcreteExercise(s: String){
        if (s.equals("Assisted Reverse Side Situp")){startAssistedReverseSideSetup()}
        else{
            Toast.makeText(this, "Exercise $s will be available coming soon!", Toast.LENGTH_SHORT).show()}
    }
    fun startAssistedReverseSideSetup() {
        val intent = Intent(this, AssistedReverseSideSetup::class.java)
        startActivity(intent)
    }
}
