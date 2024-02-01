package com.marufh.pathagar.book.dto

import com.marufh.pathagar.author.dto.AuthorResponse
import com.marufh.pathagar.category.dto.CategoryResponse
import com.marufh.pathagar.category.dto.CategoryWithoutBookDto
import com.marufh.pathagar.file.entity.FileMeta

data class BookDetailsResponse(
    var id: String? = null,
    var name: String,
    var description: String? = null,
    var pdfFile: FileMeta? = null,
    var coverImage: FileMeta? = null,
    var totalPage: Int? = null,
    var authors: Set<AuthorResponse>? = null,
    var categories: Set<CategoryResponse>? = null,
)
