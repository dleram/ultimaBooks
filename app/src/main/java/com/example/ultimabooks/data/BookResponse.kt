package com.example.booksfinal20.data

import com.google.gson.annotations.SerializedName

data class BookResponse(
    @SerializedName("items")
    val items: List<BookItem>
)

data class BookItem(
    @SerializedName("volumeInfo")
    val volumeInfo: VolumeInfo
)

data class VolumeInfo(
    @SerializedName("title")
    val title: String,
    @SerializedName("authors")
    val authors: List<String>?,
    @SerializedName("imageLinks")
    val imageLinks: ImageLinks?
)

data class ImageLinks(
    @SerializedName("thumbnail")
    val thumbnail: String
)
