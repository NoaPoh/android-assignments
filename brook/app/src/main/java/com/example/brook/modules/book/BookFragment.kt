package com.example.brook.modules.book

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.Brook.R
import com.squareup.picasso.Picasso
import kotlin.getValue

class BookFragment : Fragment() {

    private lateinit var root: View
    private lateinit var viewModel: BookViewModel
    private val args: BookFragmentArgs by navArgs()
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_book, container, false)
        viewModel = ViewModelProvider(this)[BookViewModel::class.java]
        progressBar = root.findViewById(R.id.bookSearchProgressBar)
        viewModel.setBook(args.chooseBook)

        viewModel.bookDetailsData.observe(viewLifecycleOwner) {
            setBookDetails(root)
            progressBar.visibility = View.GONE
        }
        root.findViewById<Button>(R.id.AddReviewButton)
            .setOnClickListener(::onCreateReviewButtonClick)

        progressBar.visibility = View.VISIBLE

        return root
    }

    private fun setBookDetails(root: View) {
        val bookName: TextView = root.findViewById(R.id.bookTitle)
        val bookAuthor: TextView = root.findViewById(R.id.AuthorName)
        val bookImage: ImageView = root.findViewById(R.id.bookImage)

        viewModel.bookDetailsData.let { book ->
            bookName.text = book.value?.title
            bookAuthor.text = "By " + book.value?.author
            Picasso.get().load(book.value?.coverUrl).into(bookImage)
            bookAuthor.movementMethod = ScrollingMovementMethod()
        }
    }

    private fun onCreateReviewButtonClick(view: View) {
        viewModel.bookDetailsData.let { book ->
            val action = BookFragmentDirections.actionBookFragmentToCreateReview(
                book.value?.title ?: "Book"
            )
            Navigation.findNavController(root.findViewById<Button>(R.id.AddReviewButton))
                .navigate(action)
        }
    }

}