package com.marufh.pathagar.dto

data class BookDto(
    val name: String,
    var filePath: String,
    val id: String? = null,
    val description: String? = null,
    val authors: Set<String>? = null,
)
