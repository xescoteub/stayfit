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
import com.stayfit.ui.workouts.exercises.arm.shoulder.BarbellOverheadShoulderPress

class ShoulderExercises : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shoulder_exercises)
        controlListView()
    }
    fun controlListView(){
        //create object of listview
        var listView: ListView = findViewById<View>(R.id.shoulder_list) as ListView
        //create ArrayList of String
        var arrayList: ArrayList<String> = ArrayList()
        //Add elements to arraylist
        arrayList.add("Barbell Overhead Shoulder Press")
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
        listView.setAdapter(arrayAdapter)
        //add listener to listview
        listView.setOnItemClickListener(AdapterView.OnItemClickListener { adapterView, view, i, l -> startConcreteExercise(arrayList[i].toString()) })
    }

    fun startConcreteExercise(s: String){
        if (s.equals("Barbell Overhead Shoulder Press")){startBarbellOverheadShoulderPress()}
        else{
            Toast.makeText(this, "Exercise $s will be available coming soon!", Toast.LENGTH_SHORT).show()}
    }
    fun startBarbellOverheadShoulderPress() {
        val intent = Intent(this, BarbellOverheadShoulderPress::class.java)
        startActivity(intent)
    }
}
