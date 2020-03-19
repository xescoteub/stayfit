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
import com.stayfit.ui.workouts.exercises.chest.DumbbellSqueezePress

class ChestExercises : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chest_exercises)
        controlListView()
    }
    fun controlListView(){
        //create object of listview
        var listView: ListView = findViewById<View>(R.id.chest_list) as ListView
        //create ArrayList of String
        var arrayList: ArrayList<String> = ArrayList()
        //Add elements to arraylist
        arrayList.add("Dumbbell Squeeze Press")
        arrayList.add("Incline barbell bench press")
        arrayList.add("Incline dumbbell bench press")
        arrayList.add("Close-grip barbell bench press")
        arrayList.add("Decline press-up")
        arrayList.add("Cable fly")
        arrayList.add("Decline barbell bench press")
        arrayList.add("Staggered press-up")
        arrayList.add("Chest dips")
        arrayList.add("Clap press-up")
        //Create Adapter
        val arrayAdapter: ArrayAdapter<*> = ArrayAdapter<Any>(this, android.R.layout.simple_list_item_1, arrayList as List<Any>?)
        //assign adapter to listview
        listView.setAdapter(arrayAdapter)
        //add listener to listview
        listView.setOnItemClickListener(AdapterView.OnItemClickListener { adapterView, view, i, l -> startConcreteExercise(arrayList[i].toString()) })
    }

    fun startConcreteExercise(s: String){
        if (s.equals("Dumbbell Squeeze Press")){startDumbbellSqueezePress()}
        else{
            Toast.makeText(this, "Exercise $s will be available coming soon!", Toast.LENGTH_SHORT).show()}
    }
    fun startDumbbellSqueezePress() {
        val intent = Intent(this, DumbbellSqueezePress::class.java)
        startActivity(intent)
    }
}
