package com.marufh.pathagar.category.dto

import com.marufh.pathagar.file.entity.FileMeta
import org.springframework.web.multipart.MultipartFile

data class CategoryWithoutBookDto(
    var id: String? = null,
    var file: MultipartFile? = null,
    var name: String,
    var description: String? = null,
    var imageFile: FileMeta? = null,
    var thumbFile: FileMeta? = null,
    val deleted: Boolean? = null,
)
