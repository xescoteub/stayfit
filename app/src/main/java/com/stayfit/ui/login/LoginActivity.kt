package com.stayfit.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.stayfit.MainActivity
import com.stayfit.R
import com.stayfit.config.AppPrefs
import com.stayfit.toolbar
import com.stayfit.ui.onboarding.OnBoardingActivity
import com.stayfit.ui.signup.SignUpActivity
import kotlinx.android.synthetic.main.activity_signin.*
import kotlinx.android.synthetic.main.toolbar.*


class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth

    private lateinit var viewModel : LoginViewModel

    private val TAG = "LoginActivity"

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
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        /**
         * Starts reset password activity
         */
        forgotPasswordTv.setOnClickListener {
            startActivity(Intent(this, ResetPasswordActivity::class.java))
        }

        init()
    }

    /**
     * Initializes activity
     */
    private fun init() {
        toolbar(toolBar)
        val currentUser = mAuth.currentUser
        updateUI(currentUser)

        viewModel = ViewModelProviders.of(
                this,
                LoginViewModel.LoginViewModelFactory(LoginInteractor())
        )[LoginViewModel::class.java]

        signUpTv.apply {
            text = Html.fromHtml("$text <font color='#6c63ff'>SignUp</font>")
        }
    }

    /**
     * Try to login in into app and display corresponding toast message
     */
    private fun doLogin()
    {
        if (tv_username.editText?.text.toString().isEmpty()) {
            tv_username.error = "Please enter email"
            tv_username.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(tv_username.editText?.text.toString()).matches()) {
            tv_username.error = "Please enter valid email"
            tv_username.requestFocus()
            return
        }

        if (tv_password.editText?.text.toString().isEmpty()) {
            tv_password.error = "Please enter password"
            tv_password.requestFocus()
            return
        }

        mAuth.signInWithEmailAndPassword(tv_username.editText?.text.toString(), tv_password.editText?.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmailAndPassword")
                    val user = mAuth.currentUser
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
}
