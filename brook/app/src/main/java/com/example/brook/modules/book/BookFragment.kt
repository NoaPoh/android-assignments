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


class BookFragment : Fragment() {
    private lateinit var viewModel: BookViewModel
    private val args by navArgs<BookFragmentArgs>()

    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_book, container, false)
        viewModel = ViewModelProvider(this).get(BookViewModel::class.java)
        viewModel.setBook(args.chooseBook)

        viewModel.bookDetailsData.observe(viewLifecycleOwner) {
            Log.d("TAG", "books size ${it?.size}")
            booksDetails(root)

        }
        root.findViewById<Button>(R.id.AddReviewButton).setOnClickListener {
            viewModel.bookDetailsData.let { book ->
                val action = BookFragmentDirections.actionBookFragmentToCreateReview(
                    book.value?.get(0)?.title ?: "Book"
                )
                Navigation.findNavController(root.findViewById<Button>(R.id.AddReviewButton))
                    .navigate(action)
            }
        }

        return root
    }

    private fun booksDetails(root: View) {
        val bookName: TextView = root.findViewById(R.id.bookTitle)
        val bookAuthor: TextView = root.findViewById(R.id.AuthorName)
        val bookImage: ImageView = root.findViewById(R.id.bookImage)
        val imageUrl = "https://covers.openlibrary.org/b/id/${bookImage}-L.jpg"

        viewModel.bookDetailsData.let { book ->
            bookName.text = book.value?.get(0)?.title
            bookAuthor.text = book.value?.get(0)?.author_name!![0]
            Picasso.get().load(imageUrl).into(bookImage)
            bookAuthor.movementMethod = ScrollingMovementMethod()
        }
    }

}