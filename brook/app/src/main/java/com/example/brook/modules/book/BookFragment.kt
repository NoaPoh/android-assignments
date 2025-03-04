package com.example.brook.modules.book

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.Brook.R
import com.squareup.picasso.Picasso
import kotlin.getValue

class BookFragment : Fragment() {

    private lateinit var root: View
    private lateinit var viewModel: BookViewModel
    private val args: BookFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_book, container, false)
        viewModel = ViewModelProvider(this)[BookViewModel::class.java]
        viewModel.setBook(args.chooseBook)

        viewModel.bookDetailsData.observe(viewLifecycleOwner) {
            Log.d("TAG", "books size ${it?.size}")
            setBookDetails(root)

        }
        root.findViewById<Button>(R.id.AddReviewButton)
            .setOnClickListener(::onCreateReviewButtonClick)

        return root
    }

    private fun setBookDetails(root: View) {
        val bookName: TextView = root.findViewById(R.id.bookTitle)
        val bookAuthor: TextView = root.findViewById(R.id.AuthorName)
        val bookImage: ImageView = root.findViewById(R.id.bookImage)

        viewModel.bookDetailsData.let { book ->
            bookName.text = book.value?.get(0)?.title
            bookAuthor.text = "By " + book.value?.get(0)?.author
            Picasso.get().load(book.value?.get(0)?.coverUrl).into(bookImage)
            bookAuthor.movementMethod = ScrollingMovementMethod()
        }
    }

    private fun onCreateReviewButtonClick(view: View): Unit {
        viewModel.bookDetailsData.let { book ->
            val action = BookFragmentDirections.actionBookFragmentToCreateReview(
                book.value?.get(0)?.title ?: "Book"
            )
            Navigation.findNavController(root.findViewById<Button>(R.id.AddReviewButton))
                .navigate(action)
        }
    }

}