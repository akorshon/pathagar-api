package com.marufh.pathagar.category.dto

import org.springframework.web.multipart.MultipartFile

data class CategoryCreateRequest(
    var id: String? = null,
    var file: MultipartFile? = null,
    var name: String,
    var description: String? = null,
)
