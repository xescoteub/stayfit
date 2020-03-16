package com.stayfit.ui.workouts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.stayfit.R
import com.stayfit.ui.workouts.exercises.BarbellCurl

class BicepsExercises : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biceps_exercises)
        //create object of listview
    /*
        //create object of listview
        var listView: ListView = findViewById<View>(android.R.id.list_view) as ListView
        //create ArrayList of String
        var arrayList: ArrayList<String> = ArrayList()
        //Add elements to arraylist
        arrayList.add("Barbell Curl")
        arrayList.add("Chin-Up")
        arrayList.add("Hammer Curl")
        arrayList.add("Incline Dumbbell Curl")
        arrayList.add("Supinated / Reverse Grip Bent Over Row")
        arrayList.add("Cable Curl")
        arrayList.add("Concentration Curl")
        //Create Adapter
        val arrayAdapter: ArrayAdapter<*> = ArrayAdapter<Any>(this, android.R.layout.simple_list_item_1,
            arrayList as List<Any>?
        )
        //assign adapter to listview
        listView.setAdapter(arrayAdapter)
        //add listener to listview
        listView.setOnItemClickListener(AdapterView.OnItemClickListener { adapterView, view, i, l ->
            //startBarbellCurl()
        })*/
    }


    fun startBarbellCurl(view: View) {
        val intent = Intent(this, BarbellCurl::class.java)
        startActivity(intent)
    }

}
