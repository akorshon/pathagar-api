package com.marufh.pathagar.file.dto

import com.marufh.pathagar.file.entity.FileType
import org.springframework.web.multipart.MultipartFile

data class FileDto (
    val name: String,
    val file: MultipartFile,
    val fileType: FileType,
)
