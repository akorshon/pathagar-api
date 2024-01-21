package com.marufh.pathagar.file.dto

import com.marufh.pathagar.file.entity.FileType
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Path

data class FileDto (
    val id: String? = null,
    val name: String,
    val fileType: FileType,
    val file: MultipartFile,
)
