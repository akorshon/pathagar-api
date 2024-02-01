package com.marufh.pathagar.book.dto

import com.marufh.pathagar.file.dto.FileMetaResponse

data class BookResponse(
    var id: String? = null,
    var name: String,
    var description: String? = null,
    var pdfFile: FileMetaResponse? = null,
    var coverImage: FileMetaResponse? = null,
    var totalPage: Int? = null,
)
