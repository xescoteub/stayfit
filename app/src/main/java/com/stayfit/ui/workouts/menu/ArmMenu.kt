package com.stayfit.ui.workouts.menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.stayfit.R
import com.stayfit.ui.workouts.listexercises.BicepsExercises
import com.stayfit.ui.workouts.listexercises.ShoulderExercises
import com.stayfit.ui.workouts.listexercises.TricepsExercises

class ArmMenu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_arm_menu)
    }

    fun startBicepsExercises(view: View) {
        val intent = Intent(this, BicepsExercises::class.java)
        startActivity(intent)
    }
    fun startTricepsExercises(view: View) {
        val intent = Intent(this, TricepsExercises::class.java)
        startActivity(intent)
    }
    fun startShoulderExercises(view: View) {
        val intent = Intent(this, ShoulderExercises::class.java)
        startActivity(intent)
    }
}
