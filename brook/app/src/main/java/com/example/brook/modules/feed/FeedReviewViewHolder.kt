package com.example.brook.modules.feed

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.Brook.R
import com.example.brook.data.review.Review
import com.example.brook.data.user.User
import com.squareup.picasso.Picasso

class FeedReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val reviewImageView: ImageView? = itemView.findViewById(R.id.profileReviewCardImage)
    val profileImageView: ImageView? = itemView.findViewById(R.id.ProfileImageView)
    val profileName: TextView? = itemView.findViewById(R.id.ProfileName)
    val bookName: TextView? = itemView.findViewById(R.id.BookName)
    val bookDescription: TextView? = itemView.findViewById(R.id.BookDescription)
    val reviewGrade: TextView? = itemView.findViewById(R.id.ReviewGrade)

    fun bind(review: Review?, user: User?) {
        if (review != null && user != null) {
            Picasso.get()
                .load(review.reviewImage)
                .into(reviewImageView)
            Picasso.get()
                .load(user.profileImageUrl)
                .into(profileImageView)
            val userName = "${user.firstName} ${user.lastName}"
            profileName?.text = userName
            bookName?.text = review.bookName
            bookDescription?.text = review.bookDescription
            reviewGrade?.text = "Rating: ${review.grade} ★"
        }
    }
}