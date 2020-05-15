package com.stayfit.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.stayfit.MainActivity
import com.stayfit.R
import com.stayfit.config.AppPrefs
import com.stayfit.ui.onboarding.OnBoardingActivity
import com.stayfit.ui.signup.SignUpActivity
import kotlinx.android.synthetic.main.activity_signin.*


class LoginActivity : AppCompatActivity() {

    private val TAG = "LoginActivity"

    private lateinit var mAuth : FirebaseAuth

    private lateinit var viewModel : LoginViewModel

    // Access a Cloud Firestore instance from your Activity
    val db = FirebaseFirestore.getInstance();

    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * Get current firebase auth instance
         */
        mAuth = FirebaseAuth.getInstance()

        // Check if the app is launched before
        if (AppPrefs(this).isFirstTimeLaunch()) {
            startActivity(Intent(this, OnBoardingActivity::class.java))
            finish()
        }

        setContentView(R.layout.activity_signin)

        /**
         * Calls login action
         */
        loginBtn.setOnClickListener {
            doLogin()
        }

        /**
         * Starts sign up activity
         */
        signUpTv.setOnClickListener {
//            startActivity(Intent(this, SignUpActivity::class.java))
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        /**
         * Starts reset password activity
         */
        forgotPasswordTv.setOnClickListener {
            startActivity(Intent(this, ResetPasswordActivity::class.java))
        }

        /**
         * Initialize activity
         */
        init()
    }

    /**
     * Initializes activity
     */
    private fun init()
    {
        val currentUser = mAuth.currentUser
        updateUI(currentUser)

        viewModel = ViewModelProviders.of(
                this,
                LoginViewModel.LoginViewModelFactory(LoginInteractor())
        )[LoginViewModel::class.java]
    }

    /**
     * Try to login in into app and display corresponding toast message
     */
    private fun doLogin()
    {
        if (tv_email.editText?.text.toString().isEmpty()) {
            tv_email.error = "Please enter email"
            tv_email.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(tv_email.editText?.text.toString()).matches()) {
            tv_email.error = "Please enter valid email"
            tv_email.requestFocus()
            return
        } else {
            tv_email.error = ""
        }

        if (tv_password.editText?.text.toString().isEmpty()) {
            tv_password.error = "Please enter password"
            tv_password.requestFocus()
            return
        } else {
            tv_password.error = ""
        }

        mAuth.signInWithEmailAndPassword(tv_email.editText?.text.toString(), tv_password.editText?.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmailAndPassword")
                    val user = mAuth.currentUser

                    writeNewUser()

                    updateUI(user)
                } else {
                    Log.w(TAG, "signInWithEmailAndPassword:failure", task.exception)
                    Toast.makeText(this, "Authentication failed",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    /**
     * Updates ui after login transaction is completed
     */
    private fun updateUI(currentUser: FirebaseUser?)
    {
        if (currentUser != null) {
            if(currentUser.isEmailVerified) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else{
                Toast.makeText(
                    this, "Please verify your email address.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(
                this, "Login failed.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * Inserts a new entry in users db
     */
    private fun writeNewUser()
    {
        try {
            val data = HashMap<String, Any>()
            data["user_email"] = tv_email.editText?.text.toString()

            db.collection("users").document(mAuth.uid!!).set(data).addOnFailureListener {
                    exception: java.lang.Exception -> Toast.makeText(this, exception.toString(), Toast.LENGTH_LONG).show()
            }
        } catch (e:Exception) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }
    }
}
