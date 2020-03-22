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
import com.stayfit.ui.workouts.exercises.extra.cardio.Runningstairs

class CardioExercises : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cardio_exercises)
        controlListView()
    }
    fun controlListView(){
        //create object of listview
        var listView: ListView = findViewById<View>(R.id.cardio_list) as ListView
        //create ArrayList of String
        var arrayList: ArrayList<String> = ArrayList()
        //Add elements to arraylist
        arrayList.add("Running stairs")
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
        listView.setAdapter(arrayAdapter)
        //add listener to listview
        listView.setOnItemClickListener(AdapterView.OnItemClickListener { adapterView, view, i, l -> startConcreteExercise(arrayList[i].toString()) })
    }

    fun startConcreteExercise(s: String){
        if (s.equals("Running stairs")){startRunningstairs()}
        else{
            Toast.makeText(this, "Exercise $s will be available coming soon!", Toast.LENGTH_SHORT).show()}
    }
    fun startRunningstairs() {
        val intent = Intent(this, Runningstairs::class.java)
        startActivity(intent)
    }
}
