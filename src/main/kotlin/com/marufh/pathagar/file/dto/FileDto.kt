package com.marufh.pathagar.file.dto

import com.marufh.pathagar.file.entity.FileType
import org.springframework.web.multipart.MultipartFile

data class FileDto (
    val id: String?,
    val name: String,
    val description: String?,
    val file: MultipartFile,
)
