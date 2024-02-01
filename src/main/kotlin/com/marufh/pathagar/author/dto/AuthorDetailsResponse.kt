package com.marufh.pathagar.author.dto

import com.marufh.pathagar.book.dto.BookDto
import com.marufh.pathagar.book.dto.BookResponse
import com.marufh.pathagar.file.entity.FileMeta
import org.springframework.web.multipart.MultipartFile

data class AuthorDetailsResponse(
    var id: String? = null,
    var name: String,
    var description: String? = null,
    var imageFile: FileMeta? = null,
    var thumbFile: FileMeta? = null,
    var books: List<BookResponse>? = null
)
