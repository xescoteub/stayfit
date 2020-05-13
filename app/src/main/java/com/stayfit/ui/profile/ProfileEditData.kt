package com.stayfit.ui.profile

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.stayfit.R
import com.stayfit.toast
import org.w3c.dom.Text

class ProfileEditData : AppCompatActivity() {
    private lateinit var mAuth : FirebaseAuth

    val PICK_IMAGE = 1
    val REQUEST_IMAGE_CAPTURE = 3
    var photo: String = "null"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.profile_edit_data)

        Toast.makeText(this, "EDIT TEXT CREATED", Toast.LENGTH_SHORT).show()

        mAuth = FirebaseAuth.getInstance()
        var user = mAuth.currentUser

        /*
        var email= user?.email
        var useremail : EditText = findViewById<EditText>(R.id.edit_email)
        useremail.setHint(email)

        var userid = user?.displayName
        var username: EditText = findViewById<EditText>(R.id.edit_name)
        username.setHint(userid)


        var cancel : Button = findViewById<Button>(R.id.cancelButton)
        cancel.setOnClickListener{ onBackPressed() }

        var save : Button = findViewById<Button>(R.id.saveButton)
        save.setOnClickListener{
            saveProfileChanges()
            onBackPressed()
        }

        var psswd: TextView = findViewById<TextView>(R.id.change_psswd)
        psswd.setOnClickListener{ change_psswd() }

        var photoTxt : TextView = findViewById<TextView>(R.id.change_photo)
        photoTxt.setOnClickListener{ change_photo() }*/


    }

    /*fun change_psswd() {
        //muestra un dialog para cambiar la contraseña
        var psswd: EditText = findViewById(R.id.editTextPsswd)
        var psswd_repeat: EditText = findViewById(R.id.editTextRepeat)

        mAuth = FirebaseAuth.getInstance()
        var user = mAuth.currentUser



        val psswdDialog = AlertDialog.Builder(this)
        psswdDialog.setView(R.layout.profile_change_psswd)
        psswdDialog.setTitle("Change Password")
        psswdDialog.setPositiveButton("Yes") { dialog, which ->

            if(psswd.text.equals(psswd_repeat.text)){
                //Actualizar contraseña
                /*

                mAuth = FirebaseAuth.getInstance()
                var user = mAuth.currentUser

                user.updatePassword(psswd.text).then(function() { //update succesful.
                }).catch(function(error) { // An error happened.
                })*/

                //actualizar contraseña

                user?.updatePassword(psswd.text.toString())
                ?.addOnCompleteListener { task ->
                  if (task.isSuccessful) {
                   }
               }

                Toast.makeText(this, "The password have been changed", Toast.LENGTH_LONG).show()

            }
            else{
                Toast.makeText(this, "The passwords are not equals", Toast.LENGTH_SHORT).show()
            }

        }
        psswdDialog.setNegativeButton("No") { dialog, which -> }
        psswdDialog.show()
    }*/

    fun change_photo(){
        //Cambia la foto de perfil
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

        /*
        //Actualizar la imagen


        user.updateProfile({ displayName: user.displayName , photoURL: data.getExtras.get("URI") }).then(function() {// Update successful.
        }).catch(function(error) {// An error happened.
        });
        */
    }

    fun saveProfileChanges(){

        val username : EditText = findViewById<EditText>(R.id.edit_name)
        var email : EditText = findViewById<EditText>(R.id.edit_email)

        mAuth = FirebaseAuth.getInstance()
        var user = mAuth.currentUser




        /* SI FALLA PROBAR A PONER "?" ANTES DEL PUNTO "user?.diisplayName"
        //Actualiza los datos del usuario

        var user = firebase.auth().currentUser;

        user.updateProfile({ displayName: username.getText() , photoURL: user.photoUrl}).then(function() {// Update successful.
        }).catch(function(error) {// An error happened.
        });

        user.updateEmail(email.getText()).then(function() { // Update successful.
            }).catch(function(error) { // An error happened.
            });

            */


          //actualizar username
          val profileUpdates = UserProfileChangeRequest.Builder()
              .setDisplayName(username.text.toString())
                  .build()

      user?.updateProfile(profileUpdates)
      ?.addOnCompleteListener { task ->
          if (task.isSuccessful) {
              Log.i("username ","username updated")
          }else{
              Log.i("username","username not updated")
          }
      }

        //actualizar email

        /*user?.updateEmail(email.text.toString())
     ?.addOnCompleteListener { task ->
         if (task.isSuccessful) {
           Log.i("username","email updated")
         }
     }*/

    }


}
