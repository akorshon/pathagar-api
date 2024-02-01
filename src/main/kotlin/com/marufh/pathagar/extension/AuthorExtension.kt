package com.marufh.pathagar.extension

import com.marufh.pathagar.author.dto.AuthorDetailsResponse
import com.marufh.pathagar.author.dto.AuthorResponse
import com.marufh.pathagar.author.entity.Author

fun Author.toAuthorResponse() = AuthorResponse(
        id = this.id,
        name = this.name,
        description = this.description,
        imageFile = this.imageFile,
        thumbFile = this.thumbFile,
)

fun Author.toAuthorDetailsResponse(): AuthorDetailsResponse = AuthorDetailsResponse(
    id = this.id,
    name = this.name,
    description = this.description,
    imageFile = this.imageFile,
    thumbFile = this.thumbFile,
    books = this.books?.map { it.toBookResponse() }
)