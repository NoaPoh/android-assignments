package com.example.brook.modules.createReview

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresExtension
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.Brook.R
import com.example.Brook.databinding.FragmentCreateReviewBinding

class CreateReview : Fragment() {
    private var _binding: FragmentCreateReviewBinding? = null
    private val args by navArgs<CreateReviewArgs>()
    private val binding get() = _binding!!
    private lateinit var viewModel: CreateReviewViewModel

    private lateinit var star1: ImageView
    private lateinit var star2: ImageView
    private lateinit var star3: ImageView
    private lateinit var star4: ImageView
    private lateinit var star5: ImageView

    private lateinit var imageSelectionLauncher: ActivityResultLauncher<Intent>

    private lateinit var progressBar: ProgressBar;


    @RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateReviewBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel = ViewModelProvider(this)[CreateReviewViewModel::class.java]

        star1 = binding.star1CreateReview
        star2 = binding.star2CreateReview
        star3 = binding.star3CreateReview
        star4 = binding.star4CreateReview
        star5 = binding.star5CreateReview

        progressBar = binding.progressBarCreateReview

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        setUI()

        binding.saveButton.setOnClickListener {
            this.onSaveReview()
        }
        defineImageSelectionCallBack()
        return view
    }

    private fun setUI() {

        binding.editTextReviewDescriptionCreateReview.addTextChangedListener {
            viewModel.bookDescription = it.toString().trim()
        }
        star1.tag = 1
        star2.tag = 2
        star3.tag = 3
        star4.tag = 4
        star5.tag = 5

        when (viewModel.grade) {
            in 1..5 -> {
                star1.setImageResource(if (viewModel.grade!! >= 1) R.drawable.baseline_filled_star_24 else R.drawable.baseline_empty_star_border_24)
                star2.setImageResource(if (viewModel.grade!! >= 2) R.drawable.baseline_filled_star_24 else R.drawable.baseline_empty_star_border_24)
                star3.setImageResource(if (viewModel.grade!! >= 3) R.drawable.baseline_filled_star_24 else R.drawable.baseline_empty_star_border_24)
                star4.setImageResource(if (viewModel.grade!! >= 4) R.drawable.baseline_filled_star_24 else R.drawable.baseline_empty_star_border_24)
                star5.setImageResource(if (viewModel.grade!! >= 5) R.drawable.baseline_filled_star_24 else R.drawable.baseline_empty_star_border_24)
            }
        }

        binding.star1CreateReview.setOnClickListener { onStarClicked(star1) }
        binding.star2CreateReview.setOnClickListener { onStarClicked(star2) }
        binding.star3CreateReview.setOnClickListener { onStarClicked(star3) }
        binding.star4CreateReview.setOnClickListener { onStarClicked(star4) }
        binding.star5CreateReview.setOnClickListener { onStarClicked(star5) }

        viewModel.bookDescriptionError.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) binding.layoutTextReviewDescriptionCreateReview.error = it
        }

        viewModel.imageError.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) Toast.makeText(
                requireContext(), viewModel.imageError.value, Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun onStarClicked(clickedStar: ImageView) {
        val clickedStarPosition = clickedStar.tag.toString().toInt()

        // Update the image of all stars according to the clicked star
        for (star in listOf(star1, star2, star3, star4, star5)) {
            val starPosition = star.tag.toString().toInt()
            // If the star is before or equal to the clicked star, set it to yellow, otherwise, set it to empty
            star.setImageResource(if (starPosition <= clickedStarPosition) R.drawable.baseline_filled_star_24 else R.drawable.baseline_empty_star_border_24)
        }
        viewModel.grade = clickedStarPosition
    }

    private fun onSaveReview() {
        // Check if chooseBook argument is available
        val choosedBook = args.bookName

        if (choosedBook != null) {
            binding.saveButton.isClickable = false

            viewModel.createReview(choosedBook, {
                findNavController().navigate(R.id.action_create_review_to_main_feed)
                binding.saveButton.isClickable = true
                Toast.makeText(
                    requireContext(),
                    "Review created successfully",
                    Toast.LENGTH_SHORT).show()
            }, {
                binding.saveButton.isClickable = true
            })
        } else {
            // Handle the case when chooseBook argument is missing
            Log.e("create_review", "chooseBook argument is missing")
            Toast.makeText(
                requireContext(), "Error: Choose Book argument is missing", Toast.LENGTH_SHORT
            ).show()
        }
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

        imageSelectionLauncher =
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
                        viewModel.imageURI.postValue(imageUri)
                        binding.bookPicButton.setImageURI(imageUri)
                    }
                } catch (e: Exception) {
                    Log.d("NewReview", "Error: $e")
                    Toast.makeText(
                        requireContext(), "Error processing result", Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
