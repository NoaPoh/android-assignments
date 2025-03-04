package com.example.brook.data.book

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call
import retrofit2.http.Path

data class Book(
    val id: String,
    val title: String,
    val author: String,
    val publishedDate: String?,
    val description: String?,
    val coverUrl: String?
): Serializable {}

data class BookResponse(
    @SerializedName("items") val items: List<BookItem>?
)

data class BookItem(
    @SerializedName("id") val id: String,
    @SerializedName("volumeInfo") val volumeInfo: VolumeInfo
)

data class VolumeInfo(
    @SerializedName("title") val title: String?,
    @SerializedName("authors") val authors: List<String>?,
    @SerializedName("publishedDate") val publishedDate: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("imageLinks") val imageLinks: ImageLinks?
)

data class ImageLinks(
    @SerializedName("thumbnail") val thumbnail: String?
)


interface GoogleBooksApi {
    @GET("volumes")
    fun searchBooks(
        @Query("q") query: String,  // Can be "isbn:9780143127741" or a title
        @Query("maxResults") maxResults: Int = 16,
    ): Call<BookResponse>

    @GET("volumes/{bookId}")
    fun getBookById(
        @Path("bookId") bookId: String,
    ): Call<BookItem>
}