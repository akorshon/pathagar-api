package com.marufh.pathagar.author.dto

import com.marufh.pathagar.author.entity.Author
import com.marufh.pathagar.book.dto.toBookResponse
import com.marufh.pathagar.file.dto.toFileMetaResponse

fun Author.toAuthorResponse() = AuthorResponse(
        id = this.id,
        name = this.name,
        description = this.description,
        imageFile = this.imageFile?.toFileMetaResponse(),
        thumbFile = this.thumbFile?.toFileMetaResponse(),
)

fun Author.toAuthorDetailsResponse(): AuthorDetailsResponse = AuthorDetailsResponse(
    id = this.id,
    name = this.name,
    description = this.description,
    imageFile = this.imageFile?.toFileMetaResponse(),
    thumbFile = this.thumbFile?.toFileMetaResponse(),
    books = this.books?.map { it.toBookResponse() }
)