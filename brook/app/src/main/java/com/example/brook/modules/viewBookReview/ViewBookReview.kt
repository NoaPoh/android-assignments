package com.example.brook.modules.viewBookReview

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresExtension
import androidx.navigation.fragment.navArgs
import com.example.Brook.R
import com.example.Brook.databinding.FragmentViewBookReviewBinding
import com.example.brook.data.review.ReviewModel
import com.squareup.picasso.Picasso

class ViewBookReview : Fragment() {

    private var _binding: FragmentViewBookReviewBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<ViewBookReviewArgs>()
    private lateinit var star1: ImageView
    private lateinit var star2: ImageView
    private lateinit var star3: ImageView
    private lateinit var star4: ImageView
    private lateinit var star5: ImageView

    @RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewBookReviewBinding.inflate(inflater, container, false)
        val view = binding.root
        star1 = binding.star1EditBookReview
        star2 = binding.star2EditBookReview
        star3 = binding.star3EditBookReview
        star4 = binding.star4EditBookReview
        star5 = binding.star5EditBookReview

        initFields()

        return view
    }

    private fun initFields() {
        val currentReview = args.selectedReview

        // Display review description
        binding.textBookDescription.text = currentReview.bookDescription

        // Display star rating
        when (currentReview.grade) {
            in 1..5 -> {
                star1.setImageResource(if (currentReview.grade >= 1) R.drawable.baseline_filled_star_24 else R.drawable.baseline_empty_star_border_24)
                star2.setImageResource(if (currentReview.grade >= 2) R.drawable.baseline_filled_star_24 else R.drawable.baseline_empty_star_border_24)
                star3.setImageResource(if (currentReview.grade >= 3) R.drawable.baseline_filled_star_24 else R.drawable.baseline_empty_star_border_24)
                star4.setImageResource(if (currentReview.grade >= 4) R.drawable.baseline_filled_star_24 else R.drawable.baseline_empty_star_border_24)
                star5.setImageResource(if (currentReview.grade >= 5) R.drawable.baseline_filled_star_24 else R.drawable.baseline_empty_star_border_24)
            }
        }

        // Load and display the review image
        ReviewModel.instance.getReviewImage(currentReview.id) { uri ->
            Picasso.get().load(uri).into(binding.bookPic)
        }
    }
}
