package com.example.Brook.modules.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.Brook.data.book.Book
import com.example.Brook.data.book.BookService

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
