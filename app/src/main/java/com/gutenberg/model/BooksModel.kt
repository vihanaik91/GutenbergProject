package com.gutenberg.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class BooksModel(
    val count: Int,
    val next: String? = null,
    val previous: String? = null,
    val results: Array<Results>
) : Parcelable

@Parcelize
data class Results(
    val authors: Array<Author>,
    val bookshelves: Array<String>,
    val download_count: Int,
    val formats: @RawValue Any,
    val id: Int,
    val languages: Array<String>,
    val media_type: String,
    val subjects: Array<String>,
    val title: String? = null
) : Parcelable

@Parcelize
data class Author(
    val birth_year: String? = null,
    val death_year: String? = null,
    val name: String? = null
) : Parcelable

class BookRequest{
    var page: Int = 0
}