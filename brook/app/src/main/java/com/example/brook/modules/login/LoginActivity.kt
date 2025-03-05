package com.example.brook.modules.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.Brook.R
import com.example.brook.MainActivity
import com.example.brook.modules.signup.SignupActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {

    private var auth = Firebase.auth
    private lateinit var emailAddressInputLayout: TextInputLayout
    private lateinit var emailAddressInputEditText: TextInputEditText


    private lateinit var passwordInputLayout: TextInputLayout
    private lateinit var passwordInputEditText: TextInputEditText

    private lateinit var signUpButton: Button
    private lateinit var signinButton: Button
    private lateinit var forgotPasswordButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("creation", "creating sign in screen yehonatan")
        setContentView(R.layout.login_screen)

        checkUserAndNavigate() // ✅ Check if user is logged in
    }

    private fun checkUserAndNavigate() {
        val currentUser = auth.currentUser
        if (currentUser?.uid != null) {
            Log.d("Auth", "User is already logged in. Navigating to Feed.")
            loggedInHandler()
        } else {
            setUI()
        }
    }

    fun setUI() {

        signinButton = findViewById(R.id.LoginButton)
        forgotPasswordButton = findViewById(R.id.ForgotPasswordPageButton)
        signUpButton = findViewById(R.id.toSignupPageButton)

        signinButton.setOnClickListener {
            Log.i("buttonClick", "signin button in signin screen clicked")
            checkLoginUser()
        }

        signUpButton.setOnClickListener {
            Log.i("buttonClick", "signup button in signin screen clicked")
            val intent = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(intent)
        }
        forgotPasswordButton.setOnClickListener {
            Log.i("buttonClick", "Forgot password button in signin screen clicked")
            val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }


    private fun checkLoginUser() {

        emailAddressInputLayout = findViewById(R.id.layoutLoginTextEmail)
        emailAddressInputEditText = findViewById(R.id.editLoginTextEmailAddress)
        val emailValue: String = emailAddressInputEditText.text.toString().trim()


        passwordInputEditText = findViewById(R.id.editLoginTextPassword)
        passwordInputLayout = findViewById(R.id.layoutLoginTextPassword)
        val passwordValue: String = passwordInputEditText.text.toString().trim()

        val checkUserValidation = loginUserValidation(emailValue, passwordValue)

        if (checkUserValidation) {
            Log.i("buttonClick", "signIn button in signin screen clicked")
            Log.i("signinSubmit", "email input is:" + emailValue)
            Log.i("signinSubmit", "password Input is:" + passwordValue)

            auth.signInWithEmailAndPassword(emailValue, passwordValue).addOnSuccessListener {
                loggedInHandler()
            }.addOnFailureListener {
                Toast.makeText(
                    this@LoginActivity, "Your Email or Password is incorrect!", Toast.LENGTH_SHORT
                ).show()
            }

//            auth.createUserWithEmailAndPassword(emailValue,passwordValue).addOnSuccessListener {
//                val authenticatedUser = it.user!!
//
//            }
        }


    }

    private fun loggedInHandler() {
        Toast.makeText(
            this@LoginActivity, "Welcome ${auth.currentUser?.displayName}!", Toast.LENGTH_SHORT
        ).show()
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun loginUserValidation(
        email: String, password: String
    ): Boolean {

        // Basic checks
        if (email.isEmpty()) {
            emailAddressInputLayout.error = "Email cannot be empty"
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailAddressInputLayout.error = "Invalid email format"
            return false
        } else {
            emailAddressInputLayout.error = null
        }
        if (password.isEmpty()) {
            passwordInputLayout.error = "Password cannot be empty"
            return false
        } else {
            passwordInputLayout.error = null
        }
        return true

    }
}