package com.example.brook.modules.editBookReview

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresExtension
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.Brook.R
import com.example.Brook.databinding.FragmentEditBookReviewBinding
import com.squareup.picasso.Picasso


class editBookReview : Fragment() {

    private var _binding: FragmentEditBookReviewBinding? = null

    private val binding get() = _binding!!
    private lateinit var viewModel: EditBookReviewViewModel
    private val args by navArgs<editBookReviewArgs>()
    private lateinit var star1: ImageView
    private lateinit var star2: ImageView
    private lateinit var star3: ImageView
    private lateinit var star4: ImageView
    private lateinit var star5: ImageView
    private val imageSelectionLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            try {
                val imageUri: Uri = result.data?.data!!
                val imageSize = getImageSize(imageUri)
                val maxCanvasSize = 5 * 1024 * 1024 // 5MB
                if (imageSize > maxCanvasSize) {
                    Toast.makeText(
                        requireContext(), "Selected image is too large", Toast.LENGTH_SHORT
                    ).show()
                } else {
                    viewModel.selectedImageURI.postValue(imageUri)
                    viewModel.imageChanged = true
                    binding.bookPicButton.setImageURI(imageUri)
                }
            } catch (e: Exception) {
                Log.d("EditReview", "Error: $e")
                Toast.makeText(
                    requireContext(), "Error processing result", Toast.LENGTH_SHORT
                ).show()
            }
        }

    @RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditBookReviewBinding.inflate(inflater, container, false)
        val view = binding.root
        star1 = binding.star1EditBookReview
        star2 = binding.star2EditBookReview
        star3 = binding.star3EditBookReview
        star4 = binding.star4EditBookReview
        star5 = binding.star5EditBookReview

        viewModel = ViewModelProvider(this).get(EditBookReviewViewModel::class.java)

        initFields()
        defineUpdateButtonClickListener()
        definePickImageClickListener()

        return view
    }


    @RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
    private fun definePickImageClickListener() {
        binding.bookPicButton.setOnClickListener {
            defineImageSelectionCallBack()
        }
    }

    private fun defineUpdateButtonClickListener() {
        binding.saveButton.setOnClickListener {
            binding.saveButton.isClickable = false
            viewModel.updateReview {
                findNavController().navigate(R.id.action_edit_book_review_to_profile)
                binding.saveButton.isClickable = true
            }
        }
    }

    private fun initFields() {
        val currentReview = args.selectedReview
        viewModel.loadReview(currentReview)
        binding.editTextBookDescription.setText(currentReview.bookDescription)

        binding.editTextBookDescription.addTextChangedListener {
            viewModel.bookdescription = it.toString().trim()
        }
        star1.tag = 1
        star2.tag = 2
        star3.tag = 3
        star4.tag = 4
        star5.tag = 5

        when (currentReview.grade) {
            in 1..5 -> {
                star1.setImageResource(if (viewModel.grade!! >= 1) R.drawable.baseline_filled_star_24 else R.drawable.baseline_empty_star_border_24)
                star2.setImageResource(if (viewModel.grade!! >= 2) R.drawable.baseline_filled_star_24 else R.drawable.baseline_empty_star_border_24)
                star3.setImageResource(if (viewModel.grade!! >= 3) R.drawable.baseline_filled_star_24 else R.drawable.baseline_empty_star_border_24)
                star4.setImageResource(if (viewModel.grade!! >= 4) R.drawable.baseline_filled_star_24 else R.drawable.baseline_empty_star_border_24)
                star5.setImageResource(if (viewModel.grade!! >= 5) R.drawable.baseline_filled_star_24 else R.drawable.baseline_empty_star_border_24)
            }
        }

        binding.star1EditBookReview.setOnClickListener { onStarClicked(star1) }
        binding.star2EditBookReview.setOnClickListener { onStarClicked(star2) }
        binding.star3EditBookReview.setOnClickListener { onStarClicked(star3) }
        binding.star4EditBookReview.setOnClickListener { onStarClicked(star4) }
        binding.star5EditBookReview.setOnClickListener { onStarClicked(star5) }


        viewModel.selectedImageURI.observe(viewLifecycleOwner) { uri ->
            Picasso.get().load(uri).into(binding.bookPicButton)
        }

        viewModel.descriptionError.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) binding.editTextBookDescription.error = it
        }

    }

    private fun onStarClicked(view: ImageView) {
        Log.d("StarClick", "Star clicked") // Add this line
        val clickedStar = view
        val clickedStarPosition = clickedStar.tag.toString().toInt()

        // Update the image of all stars according to the clicked star
        for (star in listOf(star1, star2, star3, star4, star5)) {
            val starPosition = star.tag.toString().toInt()
            // If the star is before or equal to the clicked star, set it to yellow, otherwise, set it to empty
            star.setImageResource(if (starPosition <= clickedStarPosition) R.drawable.baseline_filled_star_24 else R.drawable.baseline_empty_star_border_24)
        }
        viewModel.grade = clickedStarPosition
    }

    @SuppressLint("Recycle")
    private fun getImageSize(uri: Uri?): Long {
        val inputStream = requireContext().contentResolver.openInputStream(uri!!)
        return inputStream?.available()?.toLong() ?: 0
    }

    @RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
    private fun defineImageSelectionCallBack() {
        binding.bookPicButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            imageSelectionLauncher.launch(intent)
        }
    }

}