package com.marufh.pathagar.category

import com.fasterxml.jackson.annotation.JsonIgnore
import com.marufh.pathagar.base.entity.BaseEntity
import com.marufh.pathagar.book.entity.Book
import jakarta.persistence.*

@Entity
@Table(name = "category")
class Category(
    @Column(name = "name", unique = true, length = 512)
    var name: String,

    @Column(name = "deleted", columnDefinition = "tinyint(1)")
    var deleted: Boolean = false,

    @Column(name = "description", length = 2048)
    var description: String? = null,

    @Column(name = "thumbnail_path", length = 512)
    var thumbnailPath: String? = null,

    @Column(name = "image_path", length = 512)
    var imagePath: String? = null,

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "authors")
    var books: Set<Book>? = mutableSetOf(),

    ): BaseEntity()
