package com.marufh.pathagar.book.dto

import com.marufh.pathagar.author.entity.Author
import com.marufh.pathagar.file.entity.FileType

data class BookDto(
    val id: String? = null,
    val name: String,
    val description: String? = null,
    val deleted: Boolean? = null,
    val filePath: String,
    var fileType: FileType? = null,
    var hash: String? = null,
    var size: Long? = null,
    var totalPage: Int? = null,
    val coverImage: String? = null,
    var coverImagePage: Int? = null,
    val authors: Set<Author>? = null,
)
