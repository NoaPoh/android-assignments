package com.example.brook.modules.myReviews

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.Brook.R
import com.example.brook.data.review.Review
import com.example.brook.data.user.User
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.squareup.picasso.Picasso

class ProfileReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val reviewImage: ImageView? = itemView.findViewById(R.id.profileReviewCardImage)
    private val profileImage: ImageView? = itemView.findViewById(R.id.ProfileImageView)
    private val profileName: TextView? = itemView.findViewById(R.id.ProfileName)
    private val bookName: TextView? = itemView.findViewById(R.id.BookName)
    private val bookDescription: TextView? = itemView.findViewById(R.id.BookDescription)
    private val reviewGrade: TextView? = itemView.findViewById(R.id.ReviewGrade)
    private val editButton: Button = itemView.findViewById(R.id.EditButton)
    private val deleteButton: Button = itemView.findViewById(R.id.DeleteButton)

    @SuppressLint("SetTextI18n")
    fun bind(
        review: Review?,
        reviewer: User?,
        editReviewClickListener: () -> Unit,
        deleteReviewClickListener: () -> Unit
    ) {
        Log.d("TAG", "review ${review?.grade}")

        val progressBar: ProgressBar? = itemView.findViewById(R.id.imageLoadingProgressBar)
        progressBar?.visibility = View.VISIBLE

        Picasso.get().load(review?.reviewImage)
            .into(reviewImage, object : com.squareup.picasso.Callback {
                override fun onSuccess() {
                    progressBar?.visibility = View.GONE  // Hide progress bar when image loads
                }

                override fun onError(e: Exception?) {
                    progressBar?.visibility = View.GONE  // Hide progress bar if loading fails
                }
            })
        Picasso.get().load(reviewer?.profileImageUrl).into(profileImage)

        val reviewerName = "${reviewer?.firstName ?: ""} ${reviewer?.lastName ?: ""}"
        profileName?.text = reviewerName
        bookName?.text = review?.bookName
        bookDescription?.text = review?.bookDescription
        reviewGrade?.text = "Grade: ${review?.grade} ★"
        deleteButton.setOnClickListener {
            MaterialAlertDialogBuilder(itemView.context).setTitle("Delete Review")
                .setMessage("Do you want to delete this Review?")
                .setNeutralButton("Cancel") { _, _ ->
                }.setPositiveButton("Delete") { _, _ ->
                    deleteReviewClickListener()
                }.show()
        }
        editButton.setOnClickListener {
            editReviewClickListener()
        }
    }
}