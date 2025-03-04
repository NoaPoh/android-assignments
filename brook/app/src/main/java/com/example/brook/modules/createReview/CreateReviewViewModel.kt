package com.example.brook.modules.createReview

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.brook.data.review.Review
import com.example.brook.data.review.ReviewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import java.util.UUID

class CreateReviewViewModel : ViewModel() {

    var imageURI: MutableLiveData<Uri> = MutableLiveData()
    var bookDescription: String = ""
    var grade: Int? = 0
    var bookDescriptionError = MutableLiveData("")
    var gradeError = MutableLiveData("")
    var imageError = MutableLiveData("")
    private val auth = Firebase.auth

    fun createReview(
        bookName: String, createdReviewCallback: () -> Unit
    ) {
        if (validateReview()) {
            val reviewId = UUID.randomUUID().toString()
            val userId = auth.currentUser!!.uid

            val review = Review(
                userId = userId,
                id = reviewId,
                bookName = bookName,
                bookDescription = bookDescription,
                grade = grade!!,
            )

            ReviewModel.instance.addReview(review, imageURI.value!!) {
                createdReviewCallback()
            }
        }
    }

    private fun validateReview(
    ): Boolean {
        var valid = true

        if (bookDescription.isEmpty()) {
            bookDescriptionError.postValue("book description cannot be empty")
            valid = false
        }
        Log.d("NewReviewViewModel", "grade: $grade")
        if (grade == null) {
            gradeError.postValue("grade cannot be empty")
            valid = false
        } else if (grade!! < 1 || grade!! > 5) {
            gradeError.postValue("Please rate the book between 1-5 stars")
            valid = false
        }
        if (imageURI.value == null) {

            imageError.postValue("Please select an image")
            valid = false
        }

        return valid
    }
}