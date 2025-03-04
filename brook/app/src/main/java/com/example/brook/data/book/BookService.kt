package com.example.brook.data.book

import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BookService {
    companion object {
        val instance: BookService = BookService()
    }

    private val apiService: GoogleBooksApi =
        RetrofitClient.retrofit.create(GoogleBooksApi::class.java)

    fun searchBook(strBook: String, callback: (MutableList<Book>) -> Unit) {
        val encodedBookName = URLEncoder.encode(strBook, StandardCharsets.UTF_8.toString())
        val call: Call<BookResponse> = apiService.searchBooks(encodedBookName)

        call.enqueue(object : Callback<BookResponse> {
            override fun onResponse(call: Call<BookResponse>, response: Response<BookResponse>) {
                if (response.isSuccessful) {
                    val books = response.body()?.items?.mapNotNull { bookItem ->
                        bookItem.volumeInfo.title?.let {
                            Book(
                                id = bookItem.id,
                                title = bookItem.volumeInfo.title,
                                author = bookItem.volumeInfo.authors?.get(0) ?: "Unknown Author",
                                publishedDate = bookItem.volumeInfo.publishedDate,
                                description = bookItem.volumeInfo.description,
                                coverUrl = bookItem.volumeInfo.imageLinks?.thumbnail
                            )
                        }
                    } ?: mutableListOf()

                    callback(books.toMutableList())
                } else {
                    throw Exception("Failed to fetch books")
                }
            }

            override fun onFailure(call: Call<BookResponse>, t: Throwable) {
                throw Exception("Failed to fetch books")
            }
        })
    }

    fun searchBookByID(bookID: String, callback: (MutableList<Book>) -> Unit) {
        val call: Call<BookItem> = apiService.getBookById(bookID)

        call.enqueue(object : Callback<BookItem> {
            override fun onResponse(call: Call<BookItem>, response: Response<BookItem>) {
                if (response.isSuccessful) {
                    val bookData = response.body()
                    bookData?.let {
                        it.volumeInfo.title?.let { title ->
                        val book = Book(
                            id = it.id,
                            title = it.volumeInfo.title,
                            author = it.volumeInfo.authors?.get(0) ?: "Unknown Author",
                            publishedDate = it.volumeInfo.publishedDate,
                            description = it.volumeInfo.description,
                            coverUrl = it.volumeInfo.imageLinks?.thumbnail
                        )
                        callback(mutableListOf(book))
                        } ?: callback(mutableListOf())
                    } ?: callback(mutableListOf())
                } else {
                    throw Exception("Failed to fetch book")
                }
            }

            override fun onFailure(call: Call<BookItem>, t: Throwable) {
                throw Exception("Failed to fetch book")
            }
        })
    }

    object RetrofitClient {
        private const val BASE_URL = "https://www.googleapis.com/books/v1/"

        val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}