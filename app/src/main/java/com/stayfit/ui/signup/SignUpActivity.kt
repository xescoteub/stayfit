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
import com.tistory.dwfox.dwrulerviewlibrary.view.ScrollingValuePicker
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
            tv_username.error = "Please enter a username"
            tv_username.requestFocus()
            return
        }

        if (tv_email.text.toString().isEmpty()) {
            tv_email.error = "Please enter an email"
            tv_email.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(tv_email.text.toString()).matches()) {
            tv_email.error = "Please enter valid email"
            tv_email.requestFocus()
            return
        }

        if(tv_phone.text.toString().length != 9){
            tv_phone.error = "Please enter valid phone"
            tv_phone.requestFocus()
            return
        }

        if (tv_password.text.toString().isEmpty()) {
            tv_password.error = "Please enter password"
            tv_password.requestFocus()
            return
        }

        mAuth.createUserWithEmailAndPassword(tv_email.text.toString(), tv_password.text.toString())
            .addOnCompleteListener(this) { task ->

                if (task.isSuccessful) {
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                writeNewUser()
                                Log.d(TAG, "Email sent.")
                                Toast.makeText(this, "Registered successfully. Please verify your email address.", Toast.LENGTH_SHORT).show()

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
            data["user_name"] = tv_username.text.toString()
            data["user_email"] = tv_email.text.toString()
            data["user_phone"] = tv_phone.text.toString()

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
