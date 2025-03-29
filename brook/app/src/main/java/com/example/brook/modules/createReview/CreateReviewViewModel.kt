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
    var isLoading = MutableLiveData(false)

    private val auth = Firebase.auth

    fun createReview(
        bookName: String,
        createdReviewCallback: () -> Unit,
        failedToCreateCallback: (String) -> Unit
    ) {
        val validationErrors = validateReview()
        if (validationErrors.isNotEmpty()) {
            failedToCreateCallback(validationErrors.joinToString("\n"))
            return
        }

        isLoading.postValue(true)  // Show ProgressBar

        val reviewId = UUID.randomUUID().toString()
        val userId = auth.currentUser!!.uid

        val review = Review(
            userId = userId,
            id = reviewId,
            bookName = bookName,
            bookDescription = bookDescription,
            grade = grade!!
        )

        ReviewModel.instance.addReview(review, imageURI.value!!) {
            isLoading.postValue(false)  // Hide ProgressBar after saving
            createdReviewCallback()
        }
    }

    private fun validateReview(): List<String> {
        val errors = mutableListOf<String>()

        if (bookDescription.isEmpty()) {
            errors.add("Book description cannot be empty.")
        }

        if (grade == null || grade!! < 1 || grade!! > 5) {
            errors.add("Please rate the book between 1-5 stars.")
        }

        if (imageURI.value == null) {
            errors.add("Please select an image.")
        }

        return errors
    }
}
