package com.example.brook.modules.editProfile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.brook.data.user.User
import com.example.brook.data.user.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth

class EditProfileViewModel : ViewModel() {
    val userId = Firebase.auth.currentUser!!.uid
    var imageChanged = false
    var selectedImageURI: MutableLiveData<Uri> = MutableLiveData()
    var user: LiveData<User> = UserModel.instance.getCurrentUser()

    //
    var firstName: String? = "sheleg"
    var lastName: String? = null
    var firstNameError = MutableLiveData("")
    var lastNameError = MutableLiveData("")

    fun loadUser() {
        Log.d("EditProfileViewModel", "loadUser: $userId")
        if (user.value != null) {
            firstName = user.value!!.firstName
            lastName = user.value!!.lastName
        }

        UserModel.instance.getUserImage(userId) {
            selectedImageURI.postValue(it)
        }
    }

    fun updateUser(
        updatedUserCallback: () -> Unit
    ) {
        if (validateUserUpdate()) {
            val updatedUser = User(
                userId,
                firstName!!,
                lastName!!
            )

            UserModel.instance.updateUser(updatedUser) {
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setPhotoUri(selectedImageURI.value!!)
                    .setDisplayName("$firstName $lastName")
                    .build()

                Firebase.auth.currentUser!!.updateProfile(profileUpdates).addOnSuccessListener {
                    if (imageChanged) {
                        UserModel.instance.updateUserImage(userId, selectedImageURI.value!!) {
                            updatedUserCallback()
                        }
                    } else {
                        updatedUserCallback()
                    }
                }
            }
        }
    }

    private fun validateUserUpdate(
    ): Boolean {
        if (firstName!!.isEmpty()) {
            firstNameError.postValue("First name cannot be empty")
            return false
        }
        if (lastName!!.isEmpty()) {
            lastNameError.postValue("Last name cannot be empty")
            return false
        }
        return true
    }
}