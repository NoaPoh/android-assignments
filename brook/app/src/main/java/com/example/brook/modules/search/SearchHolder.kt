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
    private val bookImageView: ImageView? = itemView.findViewById(R.id.bookResultImage)
    private val bookTitleView: TextView? = itemView.findViewById(R.id.bookResultTitle)

    fun bind(book: Book?) {
        if (book == null) {
            return
        }
        itemView.setOnClickListener {
            val action = SearchFragmentDirections.actionSearchFragmentToBookFragment(book.id)
            Navigation.findNavController(itemView).navigate(action)
        }
        val imageUrl: String =
            book.coverUrl ?: "https://upload.wikimedia.org/wikipedia/commons/5/55/Question_Mark.svg"

        Picasso.get().load(imageUrl).into(bookImageView)

        bookTitleView?.text = book.title
    }
}
