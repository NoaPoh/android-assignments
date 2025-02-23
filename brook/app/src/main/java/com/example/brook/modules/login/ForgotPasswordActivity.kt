package com.example.brook.modules.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.util.Patterns
import android.widget.Toast
import com.example.Brook.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var sendEmailButton: Button
    private lateinit var rememberPassword: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("creation", "creating forgot password screen")
        setContentView(R.layout.forgot_password_screen)

        setUI()

        sendResetPasswordLink()
    }


    fun setUI() {


        rememberPassword = findViewById(R.id.ForgotPasswordRememberButton)
        sendEmailButton = findViewById(R.id.ForgotPasswordEmailLinkButton)
        rememberPassword?.setOnClickListener {
            Log.i("buttonClick", "rememberPassword button in forgot password screen clicked")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        sendEmailButton.setOnClickListener {
            Log.i("buttonClick", "send email button in forgot password screen clicked")
            val intent = Intent(this@ForgotPasswordActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun sendResetPasswordLink() {
        auth = Firebase.auth
        findViewById<Button>(R.id.ForgotPasswordEmailLinkButton).setOnClickListener {
            val email =
                findViewById<TextInputEditText>(R.id.editForgetPasswordTextEmailAddress).text.toString()
                    .trim()
            val syntaxChecksResult = validateUserEmail(email)
            if (syntaxChecksResult) {
                auth.sendPasswordResetEmail(email).addOnSuccessListener {
                    Toast.makeText(
                        this@ForgotPasswordActivity,
                        "Reset password link has been sent, Check your Email",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent =
                        Intent(this@ForgotPasswordActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(
                        this@ForgotPasswordActivity,
                        "Error: " + it.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun validateUserEmail(
        email: String
    ): Boolean {
        val lastNameInputLayout = findViewById<TextInputLayout>(R.id.layoutForgetPasswordTextEmail)
        if (email.isEmpty()) {
            lastNameInputLayout.error = "Email cannot be empty"
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            lastNameInputLayout.error = "Invalid email format"
            return false
        } else {
            lastNameInputLayout.error = null
        }
        return true
    }
}
