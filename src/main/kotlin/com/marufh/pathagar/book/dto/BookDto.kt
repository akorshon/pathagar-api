package com.marufh.pathagar.book.dto

import com.marufh.pathagar.author.entity.Author
import com.marufh.pathagar.category.Category
import com.marufh.pathagar.file.entity.FileType

data class BookDto(
    val id: String? = null,
    var name: String,
    var description: String? = null,
    var deleted: Boolean? = null,
    var filePath: String,
    var fileType: FileType? = null,
    var hash: String? = null,
    var size: Long? = null,
    var totalPage: Int? = null,
    var coverImage: String? = null,
    var coverImagePage: Int? = null,
    var authors: Set<Author>? = null,
    var categories: Set<Category>? = null,
)
