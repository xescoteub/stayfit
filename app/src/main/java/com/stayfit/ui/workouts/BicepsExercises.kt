package com.stayfit.ui.workouts

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.stayfit.ui.workouts.exercises.BarbellCurl


class BicepsExercises : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biceps_exercises) // Da error, pero si que va. No debería de darlo.
        //create object of listview

        //create object of listview
        var listView: ListView = findViewById<View>(R.id.listview) as ListView
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
        val arrayAdapter: ArrayAdapter<*> = ArrayAdapter<Any>(this, R.layout.simple_list_item_1,
            arrayList as List<Any>?
        )
        //assign adapter to listview
        listView.setAdapter(arrayAdapter)
        //add listener to listview
        listView.setOnItemClickListener(OnItemClickListener { adapterView, view, i, l ->
            //startBarbellCurl()
        })
    }
    fun startBarbellCurl(view: View) {
        val intent = Intent(this, BarbellCurl::class.java)
        startActivity(intent)
    }
}
