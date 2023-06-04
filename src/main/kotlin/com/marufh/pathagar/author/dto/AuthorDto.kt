package com.marufh.pathagar.author.dto

import com.marufh.pathagar.book.entity.Book

data class AuthorDto(
    val id: String? = null,
    val name: String,
    val description: String? = null,
    val image: String? = null,
    val thumbnail: String? = null,
    val deleted: Boolean? = null,
    val books: List<Book>? = null
)
