package com.stayfit.ui.workouts.listexercises

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.stayfit.R
import com.stayfit.ui.workouts.exercises.Exercise

class FormExercise : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_exercise)
    }

    fun addNewExercise(view: View) {
        var name: String = findViewById<EditText>(R.id.input_name).text.toString()
        var description: String = findViewById<EditText>(R.id.input_description).text.toString()
        var video: String = findViewById<EditText>(R.id.input_video).text.toString()
        if (name == null){
            Toast.makeText(this, "You have forgotten to put exercise name.", Toast.LENGTH_SHORT).show()}
        else{
            val intent = Intent()
            var arrayList: ArrayList<String> = ArrayList()
            arrayList.add(name)
            if (description!=null){ arrayList.add(description)}
            else{arrayList.add("")}
            if (video!=null){ arrayList.add(video)}
            else{arrayList.add("null")}
            intent.putExtra("LIST",arrayList)
            setResult(2,intent)
            finish()
        }
    }
    fun returnMyExercises(view: View) {
        val intent = Intent()
        setResult(0,intent)
        finish()
    }

}
