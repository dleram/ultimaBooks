package com.example.booksfinal20.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BookApi {
    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String
    ): Response<BookResponse>
}

class BookRepository(private val bookApi: BookApi) {
    suspend fun searchBooks(query: String): Response<BookResponse> {
        return bookApi.searchBooks(query)
    }
}