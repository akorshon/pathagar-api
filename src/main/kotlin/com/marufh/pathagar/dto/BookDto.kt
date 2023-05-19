package com.marufh.pathagar.dto

import com.marufh.pathagar.entity.FileType
import org.springframework.web.multipart.MultipartFile

data class BookDto(
    val name: String,
    val id: String? = null,
    val description: String? = null,
    val fileType: FileType = FileType.PDF,
    val file: MultipartFile
)
