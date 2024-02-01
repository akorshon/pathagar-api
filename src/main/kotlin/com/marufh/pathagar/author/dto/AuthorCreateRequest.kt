package com.marufh.pathagar.author.dto

import org.springframework.web.multipart.MultipartFile

data class AuthorCreateRequest(
    var id: String? = null,
    var file: MultipartFile? = null,
    var name: String,
    var description: String? = null
)
