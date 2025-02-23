package com.example.brook.modules.book

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.brook.data.book.Book
import com.example.brook.data.book.BookService

class BookViewModel : ViewModel() {
    var bookDetailsData: MutableLiveData<MutableList<Book>> = MutableLiveData()
    var bookID: String = ""

    fun setBook(bookID: String) {
        this.bookID = bookID

        BookService.instance.searchBookByID(bookID) {
            bookDetailsData.postValue(it)
        }
    }

}

