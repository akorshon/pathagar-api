package com.marufh.pathagar.book.dto

import com.marufh.pathagar.author.dto.toAuthorResponse
import com.marufh.pathagar.book.entity.Book
import com.marufh.pathagar.category.dto.toCategoryResponse
import com.marufh.pathagar.file.dto.toFileMetaResponse

fun Book.toBookResponse(): BookResponse = BookResponse(
    id = this.id,
    name = this.name,
    description = this.description,
    pdfFile = this.pdfFile?.toFileMetaResponse(),
    coverImage = this.coverImage?.toFileMetaResponse(),
    totalPage = this.totalPage,
)

fun Book.toBookDetailsResponse(): BookDetailsResponse = BookDetailsResponse(
    id = this.id,
    name = this.name,
    description = this.description,
    pdfFile = this.pdfFile?.toFileMetaResponse(),
    coverImage = this.coverImage?.toFileMetaResponse(),
    totalPage = this.totalPage,
    authors = this.authors?.map { it.toAuthorResponse() }?.toSet(),
    categories = this.categories?.map { it.toCategoryResponse() }?.toSet(),
)
