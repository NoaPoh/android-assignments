package com.example.brook.modules.myReviews

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.Brook.R
import com.example.brook.data.review.Review
import com.example.brook.data.user.User
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.squareup.picasso.Picasso

class ProfileReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val reviewImage: ImageView?
    val profileImage: ImageView?
    val profileName: TextView?
    val BookName: TextView?
    val BookDescription: TextView?
    val ReviewGrade: TextView?
    val editButton: Button
    val deleteButton: Button


    init {
        reviewImage = itemView.findViewById(R.id.profileReviewCardImage)
        profileImage = itemView.findViewById(R.id.ProfileImageView)
        profileName = itemView.findViewById(R.id.ProfileName)
        BookName = itemView.findViewById(R.id.BookName)
        BookDescription = itemView.findViewById(R.id.BookDescription)
        ReviewGrade = itemView.findViewById(R.id.ReviewGrade)
        editButton = itemView.findViewById(R.id.EditButton)
        deleteButton = itemView.findViewById(R.id.DeleteButton)
    }

    @SuppressLint("SetTextI18n")
    fun bind(
        review: Review?,
        reviewer: User?,
        editReviewClickListener: () -> Unit,
        deleteReviewClickListener: () -> Unit
    ) {
        Log.d("TAG", "review ${review?.grade}")
        Picasso.get()
            .load(review?.reviewImage)
            .into(reviewImage)
        Picasso.get()
            .load(reviewer?.profileImageUrl)
            .into(profileImage)
        val reviewerName = "${reviewer?.firstName ?: ""} ${reviewer?.lastName ?: ""}"
        profileName?.text = reviewerName
        BookName?.text = review?.bookName
        BookDescription?.text = review?.bookDescription
        ReviewGrade?.text = "Grade: ${review?.grade} ★"
        deleteButton.setOnClickListener {
            MaterialAlertDialogBuilder(itemView.context)
                .setTitle("Delete Review")
                .setMessage("Do you want to delete this Review?")
                .setNeutralButton("Cancel") { _, _ ->
                }
                .setPositiveButton("Delete") { _, _ ->
                    deleteReviewClickListener()
                }
                .show()
        }
        editButton.setOnClickListener {
            editReviewClickListener()
        }
    }
}