package com.example.Brook.modules.signup
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.provider.MediaStore
import android.net.Uri
import android.os.Build
import android.util.Patterns
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.annotation.RequiresExtension
import com.example.Brook.MainActivity
import com.example.Brook.R
import com.example.Brook.modules.login.LoginActivity
import com.example.Brook.data.user.User
import com.example.Brook.data.user.UserModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth


class SignupActivity : AppCompatActivity() {

    private val auth = Firebase.auth

    // Define callback after select image
    private lateinit var imageSelectionCallBack: ActivityResultLauncher<Intent>
    private var imageURI: Uri? = null

    // Define all the variables
    private lateinit var firstNameInputLayout: TextInputLayout
    private lateinit var firstNameEditText: TextInputEditText

    private lateinit var lastNameInputLayout: TextInputLayout
    private lateinit var lastNameInputEditText: TextInputEditText

    private lateinit var emailAddressInputLayout: TextInputLayout
    private lateinit var emailAddressInputEditText: TextInputEditText


    private lateinit var passwordInputLayout: TextInputLayout
    private lateinit var passwordInputEditText: TextInputEditText


    private lateinit var passwordConfirmationInputLayout: TextInputLayout
    private lateinit var passwordConfirmationInputEditText: TextInputEditText


     private lateinit var signUpButton: Button
     private lateinit var signInButton: Button

     private lateinit var pickProfilepicButton: ImageButton

    @RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("creation","creating signup screen")
        setContentView(R.layout.signup_screen)

        backToLogin()
        setUI()

    }

    // Define the ui of the screen
    fun setUI() {
        signUpButton = findViewById(R.id.SignupButtonSignUpScreen)
        pickProfilepicButton = findViewById(R.id.profilePicButtonSignUpScreen)
        signInButton = findViewById(R.id.AlreadyHaveAccountButtonSignUpScreen)

        // Define all button listeners
        defineImageSelectionCallBack()
        signUpButton?.setOnClickListener {
            checkNewUserDetails()
        }
        signInButton?.setOnClickListener{
            val intent= Intent(this@SignupActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        pickProfilepicButton?.setOnClickListener {
            Log.i("buttonClick", "pick profile pick button in signup screen clicked")
            openGallery()

        }
    }

    private fun backToLogin() {
        findViewById<TextView>(R.id.AlreadyHaveAccountButtonSignUpScreen).setOnClickListener {
            val intent = Intent(this@SignupActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun checkNewUserDetails() {
        firstNameEditText = findViewById(R.id.editTextFirstNameSignUpScreen)
        firstNameInputLayout = findViewById(R.id.layoutTextFirstNameSignUpScreen)
        val firstNameValue = firstNameEditText.text.toString().trim()

        lastNameInputEditText = findViewById(R.id.editTextLastNameSignUpScreen)
        lastNameInputLayout = findViewById(R.id.layoutTextLastNameSignUpScreen)
        val lastNameValue = lastNameInputEditText.text.toString().trim()

        emailAddressInputEditText = findViewById(R.id.editTextEmailAddressSignUpScreen)
        emailAddressInputLayout = findViewById(R.id.layoutTextEmailAddressSignUpScreen)
        val emailValue = emailAddressInputEditText.text.toString().trim()

        passwordInputEditText = findViewById(R.id.editTextPasswordSignUpScreen)
        passwordInputLayout = findViewById(R.id.layoutTextPasswordSignUpScreen)
        val passwordValue = passwordInputEditText.text.toString().trim()

        passwordConfirmationInputEditText = findViewById(R.id.editTextConfirmPasswordSignUpScreen)
        passwordConfirmationInputLayout = findViewById(R.id.layoutTextConfirmPasswordSignUpScreen)
        val passwordConfirmationValue = passwordConfirmationInputEditText.text.toString().trim()




        val checkUserValidation = userValidation(firstNameValue,lastNameValue,emailValue,passwordValue,passwordConfirmationValue)

        if(checkUserValidation) {
            Log.i("buttonClick", "signup button in signup screen clicked")
            Log.i("signupSubmit", "first Name Input is:" + firstNameValue)
            Log.i("signupSubmit", "Last Name Input is:" + lastNameValue)
            Log.i("signupSubmit", "email input is:" + emailValue)
            Log.i("signupSubmit", "password Input is:" + passwordValue)
            Log.i("signupSubmit", "password Confirmation Input is:" + passwordConfirmationValue)
            auth.createUserWithEmailAndPassword(emailValue,passwordValue).addOnSuccessListener {
                val authenticatedUser = it.user!!

                val profileChange = UserProfileChangeRequest.Builder()
                    .setPhotoUri(imageURI)
                    .setDisplayName("$firstNameValue $lastNameValue")
                    .build()

                authenticatedUser.updateProfile(profileChange)

                UserModel.instance.addUser(
                    User(authenticatedUser.uid, firstNameValue, lastNameValue),
                    imageURI!!
                ){
                    Toast.makeText(
                        this@SignupActivity,
                        "Register Successful",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    val intent = Intent(this@SignupActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }.addOnFailureListener {
                Toast.makeText(
                    this@SignupActivity,
                    "Register Failed, " + it.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            }
        }

    private fun userValidation(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (firstName.isEmpty()) {
            firstNameInputLayout.error = "First name cannot be empty"
            return false
        } else {
            firstNameInputLayout.error = null
        }
        if (lastName.isEmpty()) {
            lastNameInputLayout.error = "Last name cannot be empty"
            return false
        } else {
            lastNameInputLayout.error = null
        }
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
        } else if (password.length < 6) {
            passwordInputLayout.error = "Password must be at least 6 characters"
            return false
        } else if (!password.any { it.isDigit() }) {
            passwordInputLayout.error = "Password must contain at least one digit"
            return false
        } else {
            passwordInputLayout.error = null
        }
        if (confirmPassword.isEmpty()) {
            passwordConfirmationInputLayout.error = "Confirm password cannot be empty"
            return false
        } else if (password != confirmPassword) {
            passwordConfirmationInputLayout.error = "Passwords do not match."
            return false
        } else {
            passwordConfirmationInputLayout.error = null
        }
        if (imageURI == null) {
            Toast.makeText(
                this@SignupActivity,
                "You must select Profile Image",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true
    }

    // Func to move for the gallery
    private fun openGallery() {
        val intent = Intent(MediaStore.ACTION_PICK_IMAGES)
        imageSelectionCallBack.launch(intent)
    }

    @SuppressLint("Recycle")
    private fun getImageSize(uri: Uri?): Long {
        val inputStream = contentResolver.openInputStream(uri!!)
        return inputStream?.available()?.toLong() ?: 0
    }

    private fun defineImageSelectionCallBack() {
        imageSelectionCallBack = registerForActivityResult( StartActivityForResult()) { result: ActivityResult ->
            try {
                val imageUri: Uri? = result.data?.data
                if (imageUri != null) {
                    val imageSize = getImageSize(imageUri)
                    val maxCanvasSize = 5 * 1024 * 1024 // 5MB
                    if (imageSize > maxCanvasSize) {
                        Toast.makeText(
                            this@SignupActivity,
                            "Selected image is too large",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        pickProfilepicButton.setImageURI(imageUri)
                        imageURI = imageUri
                    }

                } else {
                    Toast.makeText(this@SignupActivity, "No Image Selected", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@SignupActivity, "Error processing result", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


