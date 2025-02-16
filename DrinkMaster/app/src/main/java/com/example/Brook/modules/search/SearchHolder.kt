package com.example.Brook.modules.search

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.Brook.R
import com.example.Brook.data.book.Book
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
//            val action = SearchFragmentDirections.actionSearchFragmentToBookFragment(book)
            val action = SearchFragmentDirections.actionSearchFragmentToBookFragment(book.idBook)

            Navigation.findNavController(itemView).navigate(action)
        }

        Picasso.get()
            .load(book.strBookThumb)
            .into(bookImageView)
        bookTitleView?.text = book.strBook
    }
}
