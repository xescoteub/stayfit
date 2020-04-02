package com.stayfit

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.stayfit.ui.profile.SettingsActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this);

        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)
    }

    fun settingsBtn_onClick(view: View) {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    /*fun startArmMenu(view: View) {
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
    }*/
}