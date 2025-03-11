package com.example.brook.modules.feed


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.brook.data.review.Review
import com.example.brook.data.review.ReviewModel
import com.example.brook.data.user.User
import com.example.brook.data.user.UserModel


class FeedViewModel() : ViewModel() {

    val reviews: LiveData<MutableList<Review>> = ReviewModel.instance.getAllReviews()
    val users: LiveData<MutableList<User>> = UserModel.instance.getAllUsers()
    val reviewsListLoadingState: MutableLiveData<ReviewModel.LoadingState> =
        ReviewModel.instance.reviewsListLoadingState

    fun reloadData() {
        UserModel.instance.refreshAllUsers()
        ReviewModel.instance.refreshAllReviews()
    }
}