package com.stayfit.ui.workouts.menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.stayfit.R
import com.stayfit.ui.workouts.listexercises.BicepsExercises

class ArmMenu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_arm_menu)
    }

    fun startBicepsExercises(view: View) {
        val intent = Intent(this, BicepsExercises::class.java)
        startActivity(intent)
    }
}
