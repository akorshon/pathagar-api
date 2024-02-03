package com.marufh.pathagar.book.dto

import com.marufh.pathagar.book.dto.UserBookResponse
import com.marufh.pathagar.book.dto.toBookResponse
import com.marufh.pathagar.book.entity.UserBook

fun UserBook.toUserBookResponse() = UserBookResponse(
    id = id,
    book = book.toBookResponse(),
    userEmail = userEmail,
    page = page,
    status = status,
    started = started,
    ended = ended,
    rating = rating,
    review = review
)