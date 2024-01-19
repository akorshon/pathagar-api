package com.marufh.pathagar.category

import com.marufh.pathagar.book.dto.BookDto

data class CategoryDto(
    val id: String? = null,
    var name: String,
    var description: String? = null,
    val imagePath: String? = null,
    val thumbnailPath: String? = null,
    val deleted: Boolean? = null,
    var books: List<BookDto>? = null
)
