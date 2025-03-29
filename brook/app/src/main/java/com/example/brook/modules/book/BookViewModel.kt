package com.example.brook.modules.book

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.brook.data.book.Book
import com.example.brook.data.book.BookService

class BookViewModel : ViewModel() {
    var bookDetailsData: MutableLiveData<Book> = MutableLiveData()
    var bookID: String = ""

    fun setBook(bookID: String) {
        this.bookID = bookID

        BookService.instance.searchBookByID(bookID) { books ->
            if (books.isNotEmpty()) {
                bookDetailsData.postValue(books[0]) // Store only the first book
            }
        }
    }
}

