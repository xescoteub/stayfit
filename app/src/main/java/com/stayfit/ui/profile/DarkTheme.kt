package com.stayfit.ui.profile

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import com.stayfit.R

class DarkTheme : AppCompatActivity() {

    var myswitch:Switch ?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.darkTheme)
        }else{setTheme(R.style.darkTheme)}

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dark_theme)

        myswitch=findViewById(R.id.darkTheme_switch)
        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            myswitch!!.isChecked=true
        }
        myswitch!!.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener(){ buttonView: CompoundButton, isChecked: Boolean ->
                if(isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    var i: Intent = Intent(applicationContext,DarkTheme::class.java)
                    startActivity(i)
                    finish()
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    var i: Intent = Intent(applicationContext ,DarkTheme::class.java)
                    startActivity(i)
                    finish()
            }
        })


        var passwordbtn : Button = findViewById<Button>(R.id.btnChangePasswd)
        passwordbtn.setOnClickListener{
            val psswdDialog = AlertDialog.Builder(this)
            psswdDialog.setView(R.layout.profile_change_psswd)
            psswdDialog.setTitle("Change Password")
            psswdDialog.setPositiveButton("Yes") { dialog, which ->


            }
            psswdDialog.setNegativeButton("No") { dialog, which ->

            }
            psswdDialog.show()
        }

        var emailbtn : Button = findViewById<Button>(R.id.btnChangeEmail)
        emailbtn.setOnClickListener{
            val psswdDialog = AlertDialog.Builder(this)
            psswdDialog.setView(R.layout.profile_change_email)
            psswdDialog.setTitle("Change Email")
            psswdDialog.setPositiveButton("Yes") { dialog, which ->


            }
            psswdDialog.setNegativeButton("No") { dialog, which ->

            }
            psswdDialog.show()
        }

        var usernamebtn : Button = findViewById<Button>(R.id.btnChangeUserName)
        usernamebtn.setOnClickListener{
            val psswdDialog = AlertDialog.Builder(this)
            psswdDialog.setView(R.layout.profile_change_usernamel)
            psswdDialog.setTitle("Change Email")
            psswdDialog.setPositiveButton("Yes") { dialog, which ->


            }
            psswdDialog.setNegativeButton("No") { dialog, which ->

            }
            psswdDialog.show()
        }




    }
}

