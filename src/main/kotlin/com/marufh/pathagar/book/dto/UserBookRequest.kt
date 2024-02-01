package com.marufh.pathagar.book.dto

import com.marufh.pathagar.book.entity.Book
import com.marufh.pathagar.book.entity.UserBookStatus
import java.time.Instant

data class UserBookRequest(
    val id: String? = null,
    val book: Book,
    var page:Int,
    var status: UserBookStatus,
    val started:Instant ? = null,
    val ended:Instant ? = null,
    var rating:Int? = null,
    var review:String? = null
)

