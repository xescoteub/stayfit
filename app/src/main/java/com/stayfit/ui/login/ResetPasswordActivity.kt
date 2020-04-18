package com.stayfit.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.stayfit.R
import kotlinx.android.synthetic.main.activity_reset_password.*

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private val TAG = "ResetPasswordActivity"

    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        auth = FirebaseAuth.getInstance()

        btn_reset_password.setOnClickListener {
            resetPassword()
        }
    }

    /**
     * Sends a password recovery email
     */
    private fun resetPassword()
    {
        val auth = FirebaseAuth.getInstance()

        if (tv_email_reset_password.text.toString().isEmpty()) {
            tv_email_reset_password.error = "Please enter email"
            tv_email_reset_password.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(tv_email_reset_password.text.toString()).matches()) {
            tv_email_reset_password.error = "Please enter valid email"
            tv_email_reset_password.requestFocus()
            return
        }

        auth.sendPasswordResetEmail(tv_email_reset_password.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this, "Recovery email sent!",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(
                        this, "Please enter a valid email",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
