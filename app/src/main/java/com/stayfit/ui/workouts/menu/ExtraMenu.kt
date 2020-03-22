package com.stayfit.ui.workouts.menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.stayfit.R
import com.stayfit.ui.workouts.listexercises.CardioExercises
import com.stayfit.ui.workouts.listexercises.MeditationExercises
import com.stayfit.ui.workouts.listexercises.StretchesExercises
import com.stayfit.ui.workouts.listexercises.YogaExercises

class ExtraMenu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extra_menu)
    }
    fun startCardioExercises(view: View) {
        val intent = Intent(this, CardioExercises::class.java)
        startActivity(intent)
    }
    fun startStretchesExercises(view: View) {
        val intent = Intent(this, StretchesExercises::class.java)
        startActivity(intent)
    }
    fun startYogaExercises(view: View) {
        val intent = Intent(this, YogaExercises::class.java)
        startActivity(intent)
    }
    fun startMeditationExercises(view: View) {
        val intent = Intent(this, MeditationExercises::class.java)
        startActivity(intent)
    }
}
