package com.marufh.pathagar.book.entity

import com.marufh.pathagar.base.entity.BaseEntity
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "user_book")
data class UserBook(

    @ManyToOne
    val book: Book,

    @Column(name = "user_email")
    var userEmail:String,

    @Column(name = "page")
    var page:Int,

    @Enumerated(EnumType.STRING)
    var status: UserBookStatus,

    @Column(name = "started")
    val started:Instant,

    @Column(name = "ended")
    val ended:Instant ? = null,

    @Column(name = "rating")
    var rating:Int? = null,

    @Column(name = "review", length = 2048)
    var review:String? = null

): BaseEntity()

