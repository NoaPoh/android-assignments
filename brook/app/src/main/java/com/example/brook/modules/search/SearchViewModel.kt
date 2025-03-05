package com.example.brook.modules.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.brook.data.book.Book
import com.example.brook.data.book.BookService
import com.example.brook.data.review.ReviewModel

class SearchViewModel : ViewModel() {
    var books: MutableLiveData<MutableList<Book>> = MutableLiveData()
    private val _loadingState = MutableLiveData<ReviewModel.LoadingState>()
    val loadingState: LiveData<ReviewModel.LoadingState> get() = _loadingState

    fun refreshBooks(query: String) {
        _loadingState.value = ReviewModel.LoadingState.LOADING

        BookService.instance.searchBook(query) {
            books.postValue(it)
            _loadingState.value = ReviewModel.LoadingState.LOADED
        }
    }

    fun clearBooks() {
        books.postValue(mutableListOf())
    }
}
