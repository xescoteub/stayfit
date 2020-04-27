package com.stayfit.ui.profile

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.google.firebase.auth.FirebaseAuth
import com.stayfit.R
import org.w3c.dom.Text

class ProfileEditData : AppCompatActivity() {
   // private lateinit var mAuth : FirebaseAuth

    val PICK_IMAGE = 1
    val REQUEST_IMAGE_CAPTURE = 3
    var photo: String = "null"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_edit_data)

        //val user = mAuth.currentUser

        var username : EditText = findViewById(R.id.edit_name)
        username.setHint("Username")
        var useremail : EditText = findViewById(R.id.edit_email)
        useremail.setHint("Username@gmail.com")


        var cancel : Button = findViewById<Button>(R.id.cancelButton)
        cancel.setOnClickListener{ onBackPressed() }

        var save : Button = findViewById<Button>(R.id.saveButton)
        save.setOnClickListener{ saveProfileChanges() }

        var psswd: TextView = findViewById<TextView>(R.id.change_psswd)
        psswd.setOnClickListener{ change_psswd() }

        var photoTxt : TextView = findViewById<TextView>(R.id.change_photo)
        photoTxt.setOnClickListener{ change_photo() }


    }

    fun change_psswd() {

        val psswdDialog = AlertDialog.Builder(this)
        psswdDialog.setView(R.layout.profile_change_psswd)
        psswdDialog.setTitle("Change Password")
        psswdDialog.setPositiveButton("Yes") { dialog, which ->
            //Actualizar contraseña
        }
        psswdDialog.setNegativeButton("No") { dialog, which -> }
        psswdDialog.show()
    }

    fun change_photo(){
            val getIntent = Intent(Intent.ACTION_GET_CONTENT)
            getIntent.type = "image/*"
            val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickIntent.type = "image/*"
            val chooserIntent = Intent.createChooser(getIntent, "Select Image")
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))
            startActivityForResult(chooserIntent, PICK_IMAGE)

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val imageView: ImageView = findViewById(R.id.edit_user_image)
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && null != data) {
            val uri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            val round_bitmap = RoundedBitmapDrawableFactory.create(resources,bitmap)
            round_bitmap.setCircular(true)
            imageView.setImageDrawable(round_bitmap)
            photo = uri.toString()
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data!!.extras!!.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
        }
    }

    fun saveProfileChanges(){

        onBackPressed()
    }




}
