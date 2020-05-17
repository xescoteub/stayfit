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
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.stayfit.R
import com.stayfit.ui.login.LoginActivity
import de.hdodenhof.circleimageview.CircleImageView
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
        val db = FirebaseFirestore.getInstance();
        val data = HashMap<String, Any>() // hash map para sobre escribir datos

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



        //Listener del boton para cambiar contraseña (abre un Dialog)
        var psswdbtn: Button = findViewById(R.id.btnChangePasswd)
        psswdbtn.setOnClickListener{

            val pbuilder: AlertDialog.Builder = AlertDialog.Builder(this)

            val pView: View = getLayoutInflater().inflate(R.layout.profile_change_psswd,null)
            val psswd1 : EditText = pView.findViewById(R.id.editTextPsswd)
            val psswd2 : EditText = pView.findViewById(R.id.editTextRepeat)
            val psswd_old: EditText = pView.findViewById(R.id.editTextOldPsswd)


            pbuilder.setNegativeButton( "Cancel", null)
            pbuilder.setPositiveButton("OK") { dialog, which ->


                if(psswd1.text.toString().equals((psswd2.text.toString()))){

                    updatepsswd(psswd1.text.toString(), psswd_old.text.toString())

                }

            }

            pbuilder.setTitle("Change Password")
            pbuilder.setView(pView)
            val pdialog :AlertDialog = pbuilder.create()
            pdialog.show()

        }

        var phonebtn: Button = findViewById(R.id.btnChangePhone)
        phonebtn.setOnClickListener{

            val phonebuilder: AlertDialog.Builder = AlertDialog.Builder(this)

            val phoneView: View = getLayoutInflater().inflate(R.layout.profile_change_phone,null)
            val phone_et : EditText = phoneView.findViewById(R.id.editTextPhone)

            phonebuilder.setNegativeButton( "Cancel", null)
            phonebuilder.setPositiveButton("OK") { dialog, which ->

                if(phone_et.text.toString().length != 9 ){
                    Toast.makeText(this, "Invalid Phone", Toast.LENGTH_SHORT).show()
                }else{

                    data["user_phone"] = phone_et.text.toString()
                    db.collection("users").document(mAuth.uid!!).update(data)

                }

            }

            phonebuilder.setTitle("Change Phone")
            phonebuilder.setView(phoneView)
            val pdialog :AlertDialog = phonebuilder.create()
            pdialog.show()



        }


        //Listener del boton para cambiar email (abre un Dialog)
        /*var emailbtn : Button = findViewById<Button>(R.id.btnChangeEmail)
        emailbtn.setOnClickListener{

            val ebuilder: AlertDialog.Builder = AlertDialog.Builder(this)

            val eView: View = getLayoutInflater().inflate(R.layout.profile_change_email,null)
            val email1 : EditText = eView.findViewById(R.id.editTextEmail)
            val email2 : EditText = eView.findViewById(R.id.emailRepeat)

            ebuilder.setNegativeButton( "Cancel", null)
            ebuilder.setPositiveButton("OK") { dialog, which ->

                if(email1.text.toString().equals((email2.text.toString()))){

                    data["user_email"] = email1.text.toString()
                    db.collection("users").document(mAuth.uid!!).update(data)

                    Toast.makeText(this,"IGUALES", Toast.LENGTH_SHORT).show()
                }

            }

            ebuilder.setTitle("Change Email")
            ebuilder.setView(eView)
            val edialog :AlertDialog = ebuilder.create()
            edialog.show()


        }*/

        //Listener del boton para cambiar el nombre de usuario (abre un Dialog)
        var usernamebtn : Button = findViewById<Button>(R.id.btnChangeUserName)
        usernamebtn.setOnClickListener{

            val ubuilder: AlertDialog.Builder = AlertDialog.Builder(this)

            val uView: View = getLayoutInflater().inflate(R.layout.profile_change_usernamel,null)
            val username1 : EditText = uView.findViewById(R.id.editTextUsername)

            ubuilder.setNegativeButton( "Cancel", null)
            ubuilder.setPositiveButton("OK") { dialog, which ->

                //actualizmamos datos del username en FireStore
                data["user_name"] = username1.text.toString()
                db.collection("users").document(mAuth.uid!!).update(data)


                //actualizar username en firebase
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

        var biobtn : Button = findViewById(R.id.btnChangeBio)
        biobtn.setOnClickListener{

            val bbuilder: AlertDialog.Builder = AlertDialog.Builder(this)

            val bView: View = getLayoutInflater().inflate(R.layout.profile_change_bio,null)
            val bio : EditText = bView.findViewById(R.id.editTextBio)

            bbuilder.setNegativeButton( "Cancel", null)
            bbuilder.setPositiveButton("OK") { dialog, which ->

                data["user_bio"] = bio.text.toString()
                db.collection("users").document(mAuth.uid!!).update(data)

            }

            bbuilder.setTitle("Change Username")
            bbuilder.setView(bView)
            val bdialog :AlertDialog = bbuilder.create()
            bdialog.show()

        }

        //Listener del boton para cambiar contraseña
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



        var exitbtn : Button = findViewById(R.id.exitApp)
        exitbtn.setOnClickListener{

            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to exit?").setTitle("EXIT")
                .setPositiveButton("Yes"){ dialog, which ->
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this,LoginActivity::class.java)
                    startActivity(intent)
                }
                .setNegativeButton("No"){ dialog, which ->


                }
            val dialog: AlertDialog? = builder?.create()
            dialog?.show()

        }


    }
    //funcion que actualiza la contraseña del usuario
    private fun updatepsswd(password: String, oldPassword: String) {
        mAuth = FirebaseAuth.getInstance()
        var user = mAuth.currentUser
        var email = user?.email.toString()
        var oldpsswd = oldPassword
        var newpsswd = password
        var credential = EmailAuthProvider.getCredential(email,oldpsswd)

        user?.reauthenticate(credential)?.addOnCompleteListener { task ->
            if(task.isSuccessful){

                user?.updatePassword(newpsswd).addOnCompleteListener{ task ->

                    if(task.isSuccessful){
                        Toast.makeText(this,"Password changed successfully", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this,"Password not changed successfully", Toast.LENGTH_SHORT).show()
                    }
                }

            }else{
                Toast.makeText(this,"Autentication Failed", Toast.LENGTH_SHORT).show()

            }
        }

    }



    // Pone en la FireBase del usuario su foto de perfil (URI)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        mAuth = FirebaseAuth.getInstance()
        var user = mAuth.currentUser

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && null != data) {
            val uri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            val photo_uri = getImageUri(this, bitmap)
            photo = uri.toString()

            val profileUpdates = UserProfileChangeRequest.Builder()
                .setPhotoUri(photo_uri)
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
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data!!.extras!!.get("data") as Bitmap

            val photo_uri1 = getImageUri(this, imageBitmap)

            val profileUpdates = UserProfileChangeRequest.Builder()
                .setPhotoUri(photo_uri1)
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

    }
    //convierte el bitmap en Uri
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

