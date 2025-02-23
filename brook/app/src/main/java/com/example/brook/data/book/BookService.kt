package com.example.Brook.data.book

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

    private val apiService: BookApiService = RetrofitClient.retrofit.create(BookApiService::class.java)

    fun searchBook(strBook: String, callback: (MutableList<Book>) -> Unit) {
        val encodedBookName = URLEncoder.encode(strBook, StandardCharsets.UTF_8.toString())
        val call: Call<BooksResponse> = apiService.searchBook(encodedBookName)
        call.enqueue(object : Callback<BooksResponse> {
            override fun onResponse(call: Call<BooksResponse>, response: Response<BooksResponse>) {
                if (response.isSuccessful) {
                    val books: List<Book>? = response.body()?.docs
                    callback(books?.toMutableList() ?: mutableListOf())
                } else {
                    throw Exception("Failed to fetch books")
                }
            }

            override fun onFailure(call: Call<BooksResponse>, t: Throwable) {
                throw Exception("Failed to fetch books")
            }
        })
    }

    fun searchBookByID(BookID: String, callback: (MutableList<Book>) -> Unit) {
        val call: Call<BooksResponse> = apiService.searchBookbYID(BookID)
        call.enqueue(object : Callback<BooksResponse> {
            override fun onResponse(call: Call<BooksResponse>, response: Response<BooksResponse>) {
                if (response.isSuccessful) {
                    val books: List<Book>? = response.body()?.docs
                    callback(books?.toMutableList() ?: mutableListOf())
                } else {
                    throw Exception("Failed to fetch books")
                }
            }

            override fun onFailure(call: Call<BooksResponse>, t: Throwable) {
                throw Exception("Failed to fetch books")
            }
        })
    }
    object RetrofitClient {
        private const val BASE_URL = "https://openlibrary.org/"

        val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}
