package com.example.brook.data.book

import java.io.Serializable
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call
import retrofit2.http.Path

data class Book(
    val key: String,
    val title: String,
    var author_name: Array<String>,
    val cover_i: Int?,
) : Serializable {

}

data class BooksResponse(
    val docs: List<Book>
)

interface BookApiService {
    @GET("search.json")
    fun searchBook(
        @Query("q") strBook: String,
        @Query("limit") limit: Int = 10
    ): Call<BooksResponse>

    @GET("work/{bookID}.json")
    fun searchBookByID(
        @Path("bookID") bookID: String
    ): Call<BooksResponse>
}
