package com.stayfit.ui.signup

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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


        // Gender spinner
        val spinner: Spinner = findViewById(R.id.gender_spinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.gender_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
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


        if (tv_phone.text.toString().isEmpty()) {
            tv_phone.error = "Please enter a phone number"
            tv_phone.requestFocus()
            return
        }

        if (!Patterns.PHONE.matcher(tv_phone.text.toString()).matches()) {
            tv_phone.error = "Please enter valid phone"
            tv_phone.requestFocus()
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

        if (tv_password.text.toString().isEmpty()) {
            tv_password.error = "Please enter password"
            tv_password.requestFocus()
            return
        }

        if (et_age.text.toString().isEmpty()) {
            et_age.error = "Please enter your age"
            et_age.requestFocus()
            return
        }

        if (et_height.text.toString().isEmpty()) {
            et_height.error = "Please enter your height"
            et_height.requestFocus()
            return
        }

        if (et_weight.text.toString().isEmpty()) {
            et_weight.error = "Please enter your weight"
            et_weight.requestFocus()
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
            data["user_name"]   = tv_username.text.toString()
            data["user_email"]  = tv_email.text.toString()
            data["user_phone"]  = tv_phone.text.toString()
            data["user_age"]    = et_age.text.toString()
            data["user_gender"] = gender_spinner.selectedItem.toString().toLowerCase()
            data["user_height"] = et_height.text.toString()
            data["user_weight"] = et_weight.text.toString()
            data["user_bio"] = "Im new on StayFit"

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

    private fun isValidPhone(phone: CharSequence?): Boolean {
        return if (TextUtils.isEmpty(phone)) {
            false
        } else {
            Patterns.PHONE.matcher(phone).matches()
        }
    }
}
