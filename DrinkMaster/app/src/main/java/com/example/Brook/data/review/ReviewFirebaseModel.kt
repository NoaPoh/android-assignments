package com.example.Brook.data.review

import android.net.Uri
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.memoryCacheSettings
import com.google.firebase.storage.storage

class ReviewFirebaseModel {

    private val db = Firebase.firestore
    private val storage = Firebase.storage

    companion object {
        const val REVIEWS_COLLECTION_PATH = "reviews"
    }

    init {
        val settings = firestoreSettings {
            setLocalCacheSettings(memoryCacheSettings { })
        }
        db.firestoreSettings = settings
    }


    fun getAllReviews(since: Long, callback: (List<Review>) -> Unit) {
        db.collection(REVIEWS_COLLECTION_PATH)
            .whereGreaterThanOrEqualTo(Review.LAST_UPDATED_KEY, Timestamp(since, 0))
            .get().addOnCompleteListener {
                when (it.isSuccessful) {
                    true -> {
                        val reviews: MutableList<Review> = mutableListOf()
                        for (json in it.result) {
                            val student = Review.fromJSON(json.data)
                            reviews.add(student)
                        }
                        callback(reviews)
                    }

                    false -> callback(listOf())
                }
            }
    }

    fun getImage(imageId: String, callback: (Uri) -> Unit) {
        storage.reference.child("images/$REVIEWS_COLLECTION_PATH/$imageId")
            .downloadUrl
            .addOnSuccessListener { uri ->
                callback(uri)
            }
    }

    fun addReview(review: Review, callback: () -> Unit) {
        db.collection(REVIEWS_COLLECTION_PATH).document(review.id).set(review.json)
            .addOnSuccessListener {
                callback()
            }
    }

    fun addReviewImage(reviewId: String, selectedImageUri: Uri, callback: () -> Unit) {
        val imageRef = storage.reference.child("images/$REVIEWS_COLLECTION_PATH/${reviewId}")
        imageRef.putFile(selectedImageUri).addOnSuccessListener {
            callback()
        }
    }

    fun deleteReview(review: Review?, callback: () -> Unit) {
        db.collection(REVIEWS_COLLECTION_PATH)
            .document(review!!.id).update(review.deleteJson).addOnSuccessListener {
                callback()
            }.addOnFailureListener {
                Log.d("Error", "Can't delete this review document: " + it.message)
            }
    }

    fun updateReview(review: Review?, callback: () -> Unit) {
        db.collection(REVIEWS_COLLECTION_PATH)
            .document(review!!.id).update(review.updateJson)
            .addOnSuccessListener {
                callback()
            }.addOnFailureListener {
                Log.d("Error", "Can't update this review document: " + it.message)
            }
    }
}
