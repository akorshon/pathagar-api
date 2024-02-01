package com.marufh.pathagar.category.dto

import com.marufh.pathagar.book.dto.BookResponse
import com.marufh.pathagar.file.entity.FileMeta

data class CategoryDetailsResponse(
    var id: String? = null,
    var name: String,
    var description: String? = null,
    var imageFile: FileMeta? = null,
    var thumbFile: FileMeta? = null,
    var books: Set<BookResponse>? = null
)
