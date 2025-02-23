package com.example.brook.modules.search

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.Brook.R
import com.example.brook.data.book.Book
import com.squareup.picasso.Picasso

class SearchHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val bookImageView: ImageView?
    private val bookTitleView: TextView?

    init {
        bookImageView = itemView.findViewById(R.id.bookResultImage)
        bookTitleView = itemView.findViewById(R.id.bookResultTitle)
    }

    fun bind(book: Book?) {
        if (book == null) {
            return
        }
        itemView.setOnClickListener {
            val action = SearchFragmentDirections.actionSearchFragmentToBookFragment(book.key)
            Navigation.findNavController(itemView).navigate(action)
        }
        val imageUrl: String = "https://covers.openlibrary.org/b/id/${book.cover_i}-L.jpg"

        Picasso.get()
            .load(imageUrl)
            .into(bookImageView)
        bookTitleView?.text = book.title
    }
}
