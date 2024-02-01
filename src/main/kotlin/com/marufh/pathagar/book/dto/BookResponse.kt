package com.marufh.pathagar.book.dto

import com.marufh.pathagar.file.entity.FileMeta

data class BookResponse(
    var id: String? = null,
    var name: String,
    var description: String? = null,
    var pdfFile: FileMeta? = null,
    var coverImage: FileMeta? = null,
    var totalPage: Int? = null,
)
