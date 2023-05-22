package com.marufh.pathagar.dto

import com.marufh.pathagar.entity.FileType
import org.springframework.web.multipart.MultipartFile

data class FileDto (
    val file: MultipartFile,
    val fileType: FileType,
)
