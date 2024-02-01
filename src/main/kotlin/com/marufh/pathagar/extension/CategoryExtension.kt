package com.marufh.pathagar.extension

import com.marufh.pathagar.category.dto.CategoryDetailsResponse
import com.marufh.pathagar.category.dto.CategoryResponse
import com.marufh.pathagar.category.model.Category

fun Category.toCategoryResponse() = CategoryResponse(
    id = id,
    name = name,
    description = description,
    imageFile = imageFile,
    thumbFile = thumbFile,
)

fun Category.toCategoryDetailsResponse() = CategoryDetailsResponse(
    id = id,
    name = name,
    description = description,
    imageFile = imageFile,
    thumbFile = thumbFile,
    books = books?.map { it.toBookResponse() }?.toSet(),
)