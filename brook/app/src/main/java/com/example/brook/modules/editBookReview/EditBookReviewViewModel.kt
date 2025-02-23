package com.example.Brook.modules.editBookReview

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.Brook.data.review.Review
import com.example.Brook.data.review.ReviewModel

class EditBookReviewViewModel : ViewModel() {
    var imageChanged = false
    var selectedImageURI: MutableLiveData<Uri> = MutableLiveData()
    var review: Review? = null
    var bookdescription: String? = null
    var grade: Int? = null
    var descriptionError = MutableLiveData("")
    var gradeError = MutableLiveData("")

    fun loadReview(review: Review) {
        this.review = review
        this.bookdescription = review.bookDescription
        this.grade = review.grade
        
        ReviewModel.instance.getReviewImage(review.id) {
            selectedImageURI.postValue(it)
        }
    }

    fun updateReview(
        updatedReviewCallback: () -> Unit
    ) {
        if (validateReviewUpdate()) {
            val updatedReview = Review(
                review!!.id,
                review!!.bookName,
                bookdescription!!,
                grade!!,
                review!!.userId,
            )

            ReviewModel.instance.updateReview(updatedReview) {
                if (imageChanged) {
                    ReviewModel.instance.updateReviewImage(review!!.id, selectedImageURI.value!!) {
                        updatedReviewCallback()
                    }
                } else {
                    updatedReviewCallback()
                }
            }
        }
    }

    private fun validateReviewUpdate(
    ): Boolean {
        if (bookdescription != null && bookdescription!!.isEmpty()) {
            descriptionError.postValue("Description cannot be empty")
            return false
        }
        if (grade == null) {
            gradeError.postValue("Grade cannot be empty")
            return false
        }
        return true
    }
}