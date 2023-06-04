package com.marufh.pathagar.book.entity

import com.marufh.pathagar.base.BaseEntity
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "user_book")
data class UserBook(

    @ManyToOne
    val book: Book,

    @Column(name = "page")
    var page:Int,

    @Column(name = "user_email")
    var userEmail:String,

    @Column(name = "started")
    val started:Instant,

    @Enumerated(EnumType.STRING)
    var status: UserBookStatus,

    @Column(name = "ended")
    val ended:Instant ? = null,

    var rating:Int? = null,

    var review:String? = null

): BaseEntity()

