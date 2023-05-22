package com.marufh.pathagar.dto

import org.springframework.web.multipart.MultipartFile

data class AuthorDto(
    val name: String,
    val id: String? = null,
    val description: String? = null,
    val thumbnail: String? = null,
    val image: String? = null,
    val books: Set<String>? = null,
)
