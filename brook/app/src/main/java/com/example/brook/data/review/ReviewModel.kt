package com.example.brook.data.review

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.brook.data.AppLocalDatabase
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import java.util.concurrent.Executors

class ReviewModel private constructor() {

    enum class LoadingState {
        LOADING,
        LOADED
    }

    private val database = AppLocalDatabase.db
    private var reviewsExecutor = Executors.newSingleThreadExecutor()
    private val firebaseModel = ReviewFirebaseModel()
    private val reviews: LiveData<MutableList<Review>>? = null
    val reviewsListLoadingState: MutableLiveData<LoadingState> =
        MutableLiveData(LoadingState.LOADED)


    companion object {
        val instance: ReviewModel = ReviewModel()
    }

    fun getAllReviews(): LiveData<MutableList<Review>> {
        return reviews ?: database.reviewDao().getAll()
    }

    fun getMyReviews(): LiveData<MutableList<Review>> {
        refreshAllReviews()
        return reviews ?: database.reviewDao().getReviewsByUserId(Firebase.auth.currentUser?.uid!!)
    }

    fun refreshAllReviews() {
        reviewsListLoadingState.value = LoadingState.LOADING

        val lastUpdated: Long = Review.lastUpdated

        firebaseModel.getAllReviews(lastUpdated) { list ->
            var time = lastUpdated
            for (review in list) {
                if (review.isDeleted) {
                    reviewsExecutor.execute {
                        database.reviewDao().delete(review)
                    }
                } else {
                    firebaseModel.getImage(review.id) { uri ->
                        reviewsExecutor.execute {
                            review.reviewImage = uri.toString()
                            database.reviewDao().insert(review)
                        }
                    }

                    review.timestamp?.let {
                        if (time < it)
                            time = review.timestamp ?: System.currentTimeMillis()
                    }
                    Review.lastUpdated = time
                }
            }
            reviewsListLoadingState.postValue(LoadingState.LOADED)
        }
    }

    fun addReview(review: Review, selectedImageUri: Uri, callback: () -> Unit) {
        firebaseModel.addReview(review) {
            firebaseModel.addReviewImage(review.id, selectedImageUri) {
                refreshAllReviews()
                callback()
            }
        }
    }

    fun deleteReview(review: Review?, callback: () -> Unit) {
        firebaseModel.deleteReview(review) {
            refreshAllReviews()
            callback()
        }
    }

    fun updateReview(review: Review?, callback: () -> Unit) {
        firebaseModel.updateReview(review) {
            refreshAllReviews()
            callback()
        }
    }

    fun updateReviewImage(reviewId: String, selectedImageUri: Uri, callback: () -> Unit) {
        firebaseModel.addReviewImage(reviewId, selectedImageUri) {
            refreshAllReviews()
            callback()
        }
    }

    fun getReviewImage(imageId: String, callback: (Uri) -> Unit) {
        firebaseModel.getImage(imageId, callback);
    }

}
