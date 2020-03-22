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
import com.stayfit.ui.workouts.exercises.extra.meditation.TheStimulatingBreath

class MeditationExercises : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meditation_exercises)
        controlListView()
    }
    fun controlListView(){
        //create object of listview
        var listView: ListView = findViewById<View>(R.id.meditation_list) as ListView
        //create ArrayList of String
        var arrayList: ArrayList<String> = ArrayList()
        //Add elements to arraylist
        arrayList.add("The Stimulating Breath (Bellows Breath)")
        arrayList.add("Relaxing Breathing (4-7-8) Exercise")
        arrayList.add("Counting the Breath")
        arrayList.add("The Body Scan Meditation")
        arrayList.add("Diaphragmatic Breathing")
        arrayList.add("Progressive Muscle Relaxation")
        arrayList.add("Visualization")
        arrayList.add("The Blackboard Technique")
        //Create Adapter
        val arrayAdapter: ArrayAdapter<*> = ArrayAdapter<Any>(this, android.R.layout.simple_list_item_1, arrayList as List<Any>?)
        //assign adapter to listview
        listView.setAdapter(arrayAdapter)
        //add listener to listview
        listView.setOnItemClickListener(AdapterView.OnItemClickListener { adapterView, view, i, l -> startConcreteExercise(arrayList[i].toString()) })
    }

    fun startConcreteExercise(s: String){
        if (s.equals("The Stimulating Breath (Bellows Breath)")){startTheStimulatingBreath()}
        else{
            Toast.makeText(this, "Exercise $s will be available coming soon!", Toast.LENGTH_SHORT).show()}
    }
    fun startTheStimulatingBreath() {
        val intent = Intent(this, TheStimulatingBreath::class.java)
        startActivity(intent)
    }
}
