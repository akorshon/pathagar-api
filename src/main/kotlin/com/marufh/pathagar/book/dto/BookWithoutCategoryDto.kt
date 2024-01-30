package com.marufh.pathagar.book.dto

import com.marufh.pathagar.file.entity.FileMeta
import org.springframework.web.multipart.MultipartFile

data class BookWithoutCategoryDto(
    var id: String? = null,
    var file: MultipartFile? = null,
    var name: String,
    var description: String? = null,
    var deleted: Boolean? = null,
    var pdfFile: FileMeta? = null,
    var coverImage: FileMeta? = null,
    var totalPage: Int? = null,
)
