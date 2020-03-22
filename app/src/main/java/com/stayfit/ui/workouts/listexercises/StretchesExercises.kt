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
import com.stayfit.ui.workouts.exercises.extra.stretches.StandingHamstringStretch

class StretchesExercises : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stretches_exercises)
        controlListView()
    }
    fun controlListView(){
        //create object of listview
        var listView: ListView = findViewById<View>(R.id.stretches_list) as ListView
        //create ArrayList of String
        var arrayList: ArrayList<String> = ArrayList()
        //Add elements to arraylist
        arrayList.add("Standing Hamstring Stretch")
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
        listView.setAdapter(arrayAdapter)
        //add listener to listview
        listView.setOnItemClickListener(AdapterView.OnItemClickListener { adapterView, view, i, l -> startConcreteExercise(arrayList[i].toString()) })
    }

    fun startConcreteExercise(s: String){
        if (s.equals("Standing Hamstring Stretch")){startStandingHamstringStretch()}
        else{
            Toast.makeText(this, "Exercise $s will be available coming soon!", Toast.LENGTH_SHORT).show()}
    }
    fun startStandingHamstringStretch() {
        val intent = Intent(this, StandingHamstringStretch::class.java)
        startActivity(intent)
    }
}
