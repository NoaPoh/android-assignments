package com.example.brook.modules.viewBookReview

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.brook.data.review.Review
import com.example.brook.data.review.ReviewModel

class ViewBookReviewViewModel : ViewModel() {
    var imageChanged = false
    var selectedImageURI: MutableLiveData<Uri> = MutableLiveData()
    var review: Review? = null
    var bookDescription: String? = null
    var grade: Int? = null

    fun loadReview(review: Review) {
        this.review = review
        this.bookDescription = review.bookDescription
        this.grade = review.grade

        ReviewModel.instance.getReviewImage(review.id) {
            selectedImageURI.postValue(it)
        }
    }
}