package com.example.brook.modules.search

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.Brook.R
import com.example.brook.data.book.Book
import com.squareup.picasso.Picasso

class SearchHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val bookImageView: ImageView? = itemView.findViewById(R.id.bookResultImage)
    private val loadingProgressBar: ProgressBar? = itemView.findViewById(R.id.loadingProgressBar)
    private val bookTitleView: TextView? = itemView.findViewById(R.id.bookResultTitle)

    fun bind(book: Book?) {
        if (book == null) {
            return
        }

        // Set the click listener for navigation
        itemView.setOnClickListener {
            val action = SearchFragmentDirections.actionSearchFragmentToBookFragment(book.id)
            Navigation.findNavController(itemView).navigate(action)
        }

        // Get image URL, use a default URL if the book doesn't have one
        val imageUrl: String =
            book.coverUrl ?: "https://upload.wikimedia.org/wikipedia/commons/5/55/Question_Mark.svg"

        // Show ProgressBar while the image is loading
        loadingProgressBar?.visibility = View.VISIBLE

        // Use Picasso to load the image
        Picasso.get().load(imageUrl).into(bookImageView, object : com.squareup.picasso.Callback {
                override fun onSuccess() {
                    loadingProgressBar?.visibility = View.GONE
                }

                override fun onError(e: Exception?) {
                    loadingProgressBar?.visibility = View.GONE
                }
            })

        bookTitleView?.text = book.title
    }
}

