package com.example.Brook.modules.book

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.Brook.data.book.Book
import com.example.Brook.data.book.BookService

class BookViewModel : ViewModel() {
    var bookDetailsData: MutableLiveData<MutableList<Book>> = MutableLiveData()
    var bookID : String = ""

    fun setBook(bookID: String) {
        this.bookID = bookID

        BookService.instance.searchBookByID(bookID){
            bookDetailsData.postValue(it)
  }
    }

}

