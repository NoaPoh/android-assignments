package com.example.brook.modules.feed

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.Brook.R
import com.example.brook.data.review.Review
import com.example.brook.data.user.User
import com.squareup.picasso.Picasso
import androidx.navigation.findNavController

class FeedReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val reviewImageView: ImageView? = itemView.findViewById(R.id.profileReviewCardImage)
    private val profileImageView: ImageView? = itemView.findViewById(R.id.ProfileImageView)
    private val profileName: TextView? = itemView.findViewById(R.id.ProfileName)
    private val bookName: TextView? = itemView.findViewById(R.id.BookName)
    private val bookDescription: TextView? = itemView.findViewById(R.id.BookDescription)
    private val reviewGrade: TextView? = itemView.findViewById(R.id.ReviewGrade)

    fun bind(review: Review?, user: User?) {
        if (review != null && user != null) {
            val progressBar: ProgressBar? = itemView.findViewById(R.id.imageLoadingProgressBar)
            progressBar?.visibility = View.VISIBLE  // Show progress bar before loading starts


            Picasso.get().load(review.reviewImage)
                .into(reviewImageView, object : com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        progressBar?.visibility = View.GONE  // Hide progress bar when image loads
                    }

                    override fun onError(e: Exception?) {
                        progressBar?.visibility = View.GONE  // Hide progress bar if loading fails
                    }
                })
            Picasso.get().load(user.profileImageUrl).into(profileImageView)

            val userName = "${user.firstName} ${user.lastName}"
            profileName?.text = userName
            bookName?.text = review.bookName
            bookDescription?.text = review.bookDescription
            reviewGrade?.text = "Rating: ${review.grade} ★"

            itemView.setOnClickListener {
                val action = FeedFragmentDirections.actionFeedToViewBookReview(review)
                itemView.findNavController().navigate(action)
            }
        }
    }
}