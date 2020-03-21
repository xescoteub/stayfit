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
import com.stayfit.ui.workouts.exercises.leg.StandingForwardBend

class LegExercises : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leg_exercises)
        controlListView()
    }
    fun controlListView(){
        //create object of listview
        var listView: ListView = findViewById<View>(R.id.leg_list) as ListView
        //create ArrayList of String
        var arrayList: ArrayList<String> = ArrayList()
        //Add elements to arraylist
        arrayList.add("Standing Forward Bend")
        arrayList.add("Kneeling Hip Flexor Stretch")
        arrayList.add("Leg Press")
        arrayList.add("Barbell Squats")
        arrayList.add("Jump Squat")
        arrayList.add("Hack Squat")
        arrayList.add("Romanian Deadlift")
        arrayList.add("Standing Calf Raises")
        arrayList.add("Leg Extensions")
        arrayList.add("Lying Leg Curls")
        arrayList.add("Dumbbell Step Ups")
        arrayList.add("Reverse Lunge")
        arrayList.add("Walking Lunge")
        //Create Adapter
        val arrayAdapter: ArrayAdapter<*> = ArrayAdapter<Any>(this, android.R.layout.simple_list_item_1, arrayList as List<Any>?)
        //assign adapter to listview
        listView.setAdapter(arrayAdapter)
        //add listener to listview
        listView.setOnItemClickListener(AdapterView.OnItemClickListener { adapterView, view, i, l -> startConcreteExercise(arrayList[i].toString()) })
    }
    fun startConcreteExercise(s: String){
        if (s.equals("Standing Forward Bend")){startStandingForwardBend()}
        else{
            Toast.makeText(this, "Exercise $s will be available coming soon!", Toast.LENGTH_SHORT).show()}
    }
    fun startStandingForwardBend() {
        val intent = Intent(this, StandingForwardBend::class.java)
        startActivity(intent)
    }
}
