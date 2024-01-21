package com.marufh.pathagar.author.dto

import com.marufh.pathagar.file.entity.FileMeta
import org.springframework.web.multipart.MultipartFile

data class AuthorDto(
    var id: String? = null,
    var file: MultipartFile? = null,
    var name: String,
    var description: String? = null,
    val deleted: Boolean? = null,
    var imageFile: FileMeta? = null,
    var thumbFile: FileMeta? = null,
)
