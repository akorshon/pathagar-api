package com.marufh.pathagar.book.dto

import org.springframework.web.multipart.MultipartFile

data class BookCreateRequest(
    var id: String? = null,
    var file: MultipartFile? = null,
    var name: String,
    var description: String? = null,
)
