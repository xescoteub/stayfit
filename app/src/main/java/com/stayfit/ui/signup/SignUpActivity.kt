package com.stayfit.ui.signup

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.okhttp.FormEncodingBuilder
import com.squareup.okhttp.HttpUrl
import com.stayfit.R
import com.stayfit.config.BaseHTTPAction
import com.stayfit.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUpActivity : BaseHTTPAction() {

    private val TAG = "SignUpActivity"
    
    private lateinit var mAuth: FirebaseAuth

    // Access a Cloud Firestore instance from your Activity
    val db = FirebaseFirestore.getInstance();

    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mAuth = FirebaseAuth.getInstance()

        btn_sign_up.setOnClickListener {
            signUpUser()
        }
    }

    /**
     *
     */
    private fun signUpUser()
    {
        val user = mAuth.currentUser


        if (tv_username.text.toString().isEmpty()) {
            tv_username.error = "Please enter email"
            tv_username.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(tv_username.text.toString()).matches()) {
            tv_username.error = "Please enter valid email"
            tv_username.requestFocus()
            return
        }

        if (tv_password.text.toString().isEmpty()) {
            tv_password.error = "Please enter password"
            tv_password.requestFocus()
            return
        }

        mAuth.createUserWithEmailAndPassword(tv_username.text.toString(), tv_password.text.toString())
            .addOnCompleteListener(this) { task ->




                if (task.isSuccessful) {
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                writeNewUser()
                                Log.d(TAG, "Email sent.")
                                Toast.makeText(this, "Registered successfully. Please verify your email address.",
                                    Toast.LENGTH_SHORT).show()

                                //asignar nombre de usuario y foto
                                // direccion de la imagen predeterminada: https://image.flaticon.com/icons/png/512/17/17004.png
                                val email_ = user?.email
                                if( email_ != null){

                                    val username = email_.substringBefore('@')

                                    val profileUpdates = UserProfileChangeRequest.Builder()
                                        .setDisplayName(username)
                                        .setPhotoUri(Uri.parse("https://lh3.googleusercontent.com/proxy/PNwnNDbDfI8zpWhdKndp5IAbWyjus-_GXyjDwGt7ziYSyDAbpGA4BIjBftxlz1h5X5lsjCYJWsbsYqx-qkUXptrZ5SF0z3W9BykZ0tAtvDPOK_YcRAQ"))
                                        .build()

                                    user?.updateProfile(profileUpdates)
                                        ?.addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                Log.d(TAG, "User profile updated.")
                                            }
                                        }

                                }


                            }
                        }
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Sign Up failed. Try again after some time.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    /**
     * Inserts a new entry in users db
     */
    private fun writeNewUser()
    {
        try {
            val data = HashMap<String, Any>()
            data["user_email"] = tv_username.text.toString()
            data["user_width"] = 0
            data["user_height"] = 0

            db.collection("users").document(mAuth.uid!!).set(data).addOnFailureListener {
                    exception: java.lang.Exception -> Toast.makeText(this, exception.toString(), Toast.LENGTH_LONG).show()
            }

        } catch (e:Exception) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }
    }

    override fun responseRunnable(responseStr: String?): Runnable?
    {
        return Runnable {
            println("Blog inserted : $responseStr")
        }
    }
}
