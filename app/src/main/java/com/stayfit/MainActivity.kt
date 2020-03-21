package com.stayfit

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
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

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_feed,
                R.id.navigation_activity,
                R.id.navigation_workouts,
                R.id.navigation_profile
            )
        )
        //setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
    fun startArmMenu(view: View) {
        val intent = Intent(this, ArmMenu::class.java)
        startActivity(intent)
    }
    fun startLegExercises(view: View) {
        val intent = Intent(this, LegExercises::class.java)
        startActivity(intent)
    }
    fun startAbsExercises(view: View) {
        val intent = Intent(this, AbsExercises::class.java)
        startActivity(intent)
    }
    fun startChestExercises(view: View) {
        val intent = Intent(this, ChestExercises::class.java)
        startActivity(intent)
    }
    fun startBackExercises(view: View) {
        val intent = Intent(this, BackExercises::class.java)
        startActivity(intent)
    }
    fun startExtraMenu(view: View) {
        val intent = Intent(this, ExtraMenu::class.java)
        startActivity(intent)
    }
}