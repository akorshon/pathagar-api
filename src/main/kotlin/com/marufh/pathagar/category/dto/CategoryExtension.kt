package com.marufh.pathagar.category.dto

import com.marufh.pathagar.book.dto.toBookResponse
import com.marufh.pathagar.category.model.Category
import com.marufh.pathagar.file.dto.toFileMetaResponse

fun Category.toCategoryResponse() = CategoryResponse(
    id = id,
    name = name,
    description = description,
    imageFile = imageFile?.toFileMetaResponse(),
    thumbFile = thumbFile?.toFileMetaResponse(),
)

fun Category.toCategoryDetailsResponse() = CategoryDetailsResponse(
    id = id,
    name = name,
    description = description,
    imageFile = imageFile?.toFileMetaResponse(),
    thumbFile = thumbFile?.toFileMetaResponse(),
    books = books?.map { it.toBookResponse() }?.toSet(),
)