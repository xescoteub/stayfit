package com.stayfit

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.stayfit.ui.login.LoginActivity
import com.stayfit.ui.profile.ProfileFragment
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
    }

    fun settingsBtn_onClick(view: View) {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }
    fun logOut_onClick(view: View) {

        val alertdialog_logout = AlertDialog.Builder(this)
        alertdialog_logout.setTitle("Salida")
        alertdialog_logout.setMessage("Seguro desea salir?")
        alertdialog_logout.setPositiveButton("Yes") { dialog, which ->
            finish()
        }
        alertdialog_logout.setNegativeButton("No"){ dialog, which -> }

        alertdialog_logout.show()

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