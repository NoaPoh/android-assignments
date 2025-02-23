package com.example.brook.modules.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.brook.data.book.Book
import com.example.brook.data.book.BookService

class SearchViewModel : ViewModel() {
    var books: MutableLiveData<MutableList<Book>> = MutableLiveData()

    fun refreshBooks(query: String) {
        BookService.instance.searchBook(query) {
            books.postValue(it)
        }
    }

    fun clearBooks() {
        books.postValue(mutableListOf())
    }
}
