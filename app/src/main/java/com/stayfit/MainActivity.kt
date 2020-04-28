package com.stayfit

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.stayfit.ui.myroutines.FormRoutine
import com.stayfit.ui.profile.DarkTheme
import com.stayfit.ui.profile.SettingsActivity
import com.stayfit.ui.workouts.listexercises.AbsExercises
import com.stayfit.ui.workouts.listexercises.BackExercises
import com.stayfit.ui.workouts.listexercises.ChestExercises
import com.stayfit.ui.workouts.menu.ArmMenu
import com.stayfit.ui.workouts.menu.ExtraMenu
import com.stayfit.ui.workouts.listexercises.LegExercises

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)

        // Change status bar color
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorIndigo)
    }

    fun settingsBtn_onClick(view: View) {
        val intent = Intent(this, DarkTheme::class.java)
        startActivity(intent)
    }
}