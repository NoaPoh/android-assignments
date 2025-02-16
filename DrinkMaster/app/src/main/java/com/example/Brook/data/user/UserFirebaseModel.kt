package com.example.Brook.data.user

import android.net.Uri
import android.util.Log
import com.example.Brook.data.user.User
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.memoryCacheSettings
import com.google.firebase.storage.storage

class UserFirebaseModel {

    private val db = Firebase.firestore
    private val storage = Firebase.storage

    companion object {
        const val USERS_COLLECTION_PATH = "users"
    }

    init {
        val settings = firestoreSettings {
            setLocalCacheSettings(memoryCacheSettings { })
        }
        db.firestoreSettings = settings
    }

    fun getAllUsers(since: Long, callback: (List<User>) -> Unit) {
        db.collection(USERS_COLLECTION_PATH)
            .whereGreaterThanOrEqualTo(User.LAST_MODIFIED_KEY, Timestamp(since, 0))
            .get().addOnCompleteListener {
                when (it.isSuccessful) {
                    true -> {
                        val users: MutableList<User> = mutableListOf()
                        for (json in it.result) {
                            val user = User.fromJSON(json.data)
                            users.add(user)
                        }
                        callback(users)
                    }

                    false -> callback(listOf())
                }
            }
    }

    fun getImage(imageId: String, callback: (Uri) -> Unit) {
        storage.reference.child("images/$USERS_COLLECTION_PATH/$imageId")
            .downloadUrl
            .addOnSuccessListener { uri ->
                callback(uri)
            }
    }


    fun addUserImage(userId: String, selectedImageUri: Uri, callback: () -> Unit) {
        val imageRef = storage.reference.child("images/$USERS_COLLECTION_PATH/${userId}")
        imageRef.putFile(selectedImageUri).addOnSuccessListener {
            callback()
        }
    }

    fun updateUser(user: User?, callback: () -> Unit) {
        db.collection(USERS_COLLECTION_PATH)
            .document(user!!.id).update(user.updateJson)
            .addOnSuccessListener {
                callback()
            }.addOnFailureListener {
                Log.d("Error", "Can't update this user document: " + it.message)
            }
    }

    fun addUser(user: User, callback: () -> Unit) {
        Log.i("userFirebaseModel", "trying to create user:" + user)
        try {
            db.collection(USERS_COLLECTION_PATH).document(user.id).set(user.json)
                .addOnSuccessListener {
                    Log.i("userFirebaseModel", "success to create user:" + user)
                    callback()
                }
        } catch (e: Exception) {
            Log.d("userFirebaseModel", "Error: $e")
        }
    }
}
