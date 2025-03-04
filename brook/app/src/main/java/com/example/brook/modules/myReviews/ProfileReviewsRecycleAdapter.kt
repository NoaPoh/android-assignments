package com.example.brook.modules.myReviews

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.Brook.R
import com.example.brook.data.review.Review
import com.example.brook.data.review.ReviewModel
import com.example.brook.data.user.User

class ProfileReviewsRecycleAdapter(var reviews: MutableList<Review>?, var user: User?) :
    RecyclerView.Adapter<ProfileReviewViewHolder>() {

    override fun getItemCount(): Int {
        return reviews?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileReviewViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_my_review_card, parent, false)
        return ProfileReviewViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProfileReviewViewHolder, position: Int) {
        val review = reviews?.get(position)
        Log.d("TAG", "reviews size ${reviews?.size}")
        holder.bind(review, user, {
            val action = ProfileReviewsDirections.actionProfileReviewsToEditBookReview(review!!)
            Navigation.findNavController(holder.itemView).navigate(action)
        },
            {
                ReviewModel.instance.deleteReview(review) {
                    Toast.makeText(
                        holder.itemView.context,
                        "Review deleted!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

}