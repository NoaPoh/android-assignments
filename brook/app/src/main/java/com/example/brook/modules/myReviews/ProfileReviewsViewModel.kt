package com.example.Brook.modules.myReviews

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.Brook.data.review.Review
import com.example.Brook.data.review.ReviewModel
import com.example.Brook.data.user.User
import com.example.Brook.data.user.UserModel

class ProfileReviewsViewModel : ViewModel() {
    val reviews: LiveData<MutableList<Review>> = ReviewModel.instance.getMyReviews()
    val user: LiveData<User> = UserModel.instance.getCurrentUser()
    val reviewsListLoadingState: MutableLiveData<ReviewModel.LoadingState> =
        ReviewModel.instance.reviewsListLoadingState

    fun reloadData() {
        UserModel.instance.refreshAllUsers()
        ReviewModel.instance.refreshAllReviews()
    }
}