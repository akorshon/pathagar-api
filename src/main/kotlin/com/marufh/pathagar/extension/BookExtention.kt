package com.marufh.pathagar.extension

import com.marufh.pathagar.book.dto.BookDetailsResponse
import com.marufh.pathagar.book.dto.BookResponse
import com.marufh.pathagar.book.entity.Book

fun Book.toBookResponse(): BookResponse = BookResponse(
    id = this.id,
    name = this.name,
    description = this.description,
    pdfFile = this.pdfFile,
    coverImage = this.coverImage,
    totalPage = this.totalPage,
)

fun Book.toBookDetailsResponse(): BookDetailsResponse = BookDetailsResponse(
    id = this.id,
    name = this.name,
    description = this.description,
    pdfFile = this.pdfFile,
    coverImage = this.coverImage,
    totalPage = this.totalPage,
    authors = this.authors?.map { it.toAuthorResponse() }?.toSet(),
    categories = this.categories?.map { it.toCategoryResponse() }?.toSet(),
)
