package com.stayfit.ui.workouts.listexercises

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.stayfit.R
import com.stayfit.ui.workouts.exercises.Exercise
import kotlinx.android.synthetic.main.activity_form_exercise.*

class FormExercise : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_exercise)
    }

    fun addNewExercise(view: View) {
        val textInputLayoutName = findViewById<TextInputLayout>(R.id.input_name)
        val name: String = textInputLayoutName.editText!!.text.toString()
        val textInputLayoutDescription = findViewById<TextInputLayout>(R.id.input_description)
        val description: String = textInputLayoutDescription.editText!!.text.toString()
        val textInputLayoutVideo = findViewById<TextInputLayout>(R.id.input_video)
        val video: String = textInputLayoutVideo.editText!!.text.toString()
        if (name.equals("")){ input_name.error = resources.getString(R.string.invalid_name)}
        else{
            val intent = Intent()
            var arrayList: ArrayList<String> = ArrayList()
            arrayList.add(name)
            arrayList.add(description)
            arrayList.add(video)
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
