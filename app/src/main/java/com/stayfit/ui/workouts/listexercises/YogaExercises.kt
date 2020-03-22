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
import com.stayfit.ui.workouts.exercises.extra.yoga.Tadasana

class YogaExercises : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yoga_exercises)
        controlListView()
    }
    fun controlListView(){
        //create object of listview
        var listView: ListView = findViewById<View>(R.id.yoga_list) as ListView
        //create ArrayList of String
        var arrayList: ArrayList<String> = ArrayList()
        //Add elements to arraylist
        arrayList.add("Mountain Pose (Tadasana)")
        arrayList.add("Raised Arms Pose (Urdhva Hastansana)")
        arrayList.add("Standing Forward Bend (Uttanasana)")
        arrayList.add("Garland Pose (Malasana)")
        arrayList.add("Lunge Pose")
        arrayList.add("Plank Pose")
        arrayList.add("Staff Pose")
        arrayList.add("Seated Forward Bend (Paschimottanasana)")
        arrayList.add("Head to Knee Pose (Janu Sirsasana)")
        arrayList.add("Happy Baby Pose (Ananda Balasana)")
        //Create Adapter
        val arrayAdapter: ArrayAdapter<*> = ArrayAdapter<Any>(this, android.R.layout.simple_list_item_1, arrayList as List<Any>?)
        //assign adapter to listview
        listView.setAdapter(arrayAdapter)
        //add listener to listview
        listView.setOnItemClickListener(AdapterView.OnItemClickListener { adapterView, view, i, l -> startConcreteExercise(arrayList[i].toString()) })
    }

    fun startConcreteExercise(s: String){
        if (s.equals("Mountain Pose (Tadasana)")){startTadasana()}
        else{
            Toast.makeText(this, "Exercise $s will be available coming soon!", Toast.LENGTH_SHORT).show()}
    }
    fun startTadasana() {
        val intent = Intent(this, Tadasana::class.java)
        startActivity(intent)
    }
}
