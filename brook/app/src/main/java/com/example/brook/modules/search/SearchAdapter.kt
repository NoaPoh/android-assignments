package com.example.brook.modules.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.Brook.R
import com.example.brook.data.book.Book

class SearchAdapter(var books: MutableList<Book>?) :
    RecyclerView.Adapter<SearchHolder>() {

    override fun getItemCount(): Int {
        return books?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.book_search_card, parent, false)
        return SearchHolder(itemView)
    }

    override fun onBindViewHolder(holder: SearchHolder, position: Int) {
        val book = books?.get(position)
        holder.bind(book)
    }
}
