package com.marufh.pathagar.author.dto

import com.marufh.pathagar.book.dto.BookDto

data class AuthorDto(
    val id: String? = null,
    var name: String,
    var description: String? = null,
    val imagePath: String? = null,
    val thumbnailPath: String? = null,
    val deleted: Boolean? = null,
    var books: List<BookDto>? = null
)
