package com.stayfit.ui.profile

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.stayfit.R
import java.io.ByteArrayOutputStream

class DarkTheme : AppCompatActivity() {
    private lateinit var mAuth : FirebaseAuth

    var myswitch:Switch ?= null

    val PICK_IMAGE = 1
    val REQUEST_IMAGE_CAPTURE = 3
    var photo: String = "null"


    override fun onCreate(savedInstanceState: Bundle?) {
        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.darkTheme)
        }else{setTheme(R.style.darkTheme)}

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dark_theme)

        mAuth = FirebaseAuth.getInstance()
        var user = mAuth.currentUser

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
        var psswdbtn: Button = findViewById(R.id.btnChangePasswd)
        psswdbtn.setOnClickListener{

            val pbuilder: AlertDialog.Builder = AlertDialog.Builder(this)

            val pView: View = getLayoutInflater().inflate(R.layout.profile_change_psswd,null)
            val psswd1 : EditText = pView.findViewById(R.id.editTextPsswd)
            val psswd2 : EditText = pView.findViewById(R.id.editTextRepeat)

            pbuilder.setNegativeButton( "Cancel", null)
            pbuilder.setPositiveButton("OK") { dialog, which ->

                if(psswd1.text.toString().equals((psswd2.text.toString()))){
                    Toast.makeText(this,"IGUALES", Toast.LENGTH_SHORT).show()
                }

            }

            pbuilder.setTitle("Change Password")
            pbuilder.setView(pView)
            val pdialog :AlertDialog = pbuilder.create()
            pdialog.show()



        }



        var emailbtn : Button = findViewById<Button>(R.id.btnChangeEmail)
        emailbtn.setOnClickListener{

            val ebuilder: AlertDialog.Builder = AlertDialog.Builder(this)

            val eView: View = getLayoutInflater().inflate(R.layout.profile_change_email,null)
            val email1 : EditText = eView.findViewById(R.id.editTextEmail)
            val email2 : EditText = eView.findViewById(R.id.emailRepeat)

            ebuilder.setNegativeButton( "Cancel", null)
            ebuilder.setPositiveButton("OK") { dialog, which ->

                if(email1.text.toString().equals((email2.text.toString()))){
                    Toast.makeText(this,"IGUALES", Toast.LENGTH_SHORT).show()
                }

            }

            ebuilder.setTitle("Change Email")
            ebuilder.setView(eView)
            val edialog :AlertDialog = ebuilder.create()
            edialog.show()


        }

        var usernamebtn : Button = findViewById<Button>(R.id.btnChangeUserName)
        usernamebtn.setOnClickListener{

            val ubuilder: AlertDialog.Builder = AlertDialog.Builder(this)

            val uView: View = getLayoutInflater().inflate(R.layout.profile_change_usernamel,null)
            val username1 : EditText = uView.findViewById(R.id.editTextUsername)

            ubuilder.setNegativeButton( "Cancel", null)
            ubuilder.setPositiveButton("OK") { dialog, which ->

                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(username1.text.toString())
                    .build()

                user?.updateProfile(profileUpdates)
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.i("username ","username updated")
                        }else{
                            Log.i("username","username not updated")
                        }
                    }

            }

            ubuilder.setTitle("Change Username")
            ubuilder.setView(uView)
            val udialog :AlertDialog = ubuilder.create()
            udialog.show()

        }

        var photobtn : Button = findViewById(R.id.btnChangePhoto)
        photobtn.setOnClickListener{

            val getIntent = Intent(Intent.ACTION_GET_CONTENT)
            getIntent.type = "image/*"
            val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickIntent.type = "image/*"
            val chooserIntent = Intent.createChooser(getIntent, "Select Image")
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))
            startActivityForResult(chooserIntent, PICK_IMAGE)



        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val imageView: ImageView = findViewById(R.id.imagenprueba)

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && null != data) {
            val uri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            val round_bitmap = RoundedBitmapDrawableFactory.create(resources, bitmap)
            round_bitmap.setCircular(true)
            imageView.setImageDrawable(round_bitmap)
            photo = uri.toString()
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data!!.extras!!.get("data") as Bitmap
            val round_bitmap = RoundedBitmapDrawableFactory.create(resources, imageBitmap)
            round_bitmap.setCircular(true)
            imageView.setImageDrawable(round_bitmap)
        }

    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.getContentResolver(),
            inImage,
            "Title",
            null
        )
        return Uri.parse(path)
    }



}

