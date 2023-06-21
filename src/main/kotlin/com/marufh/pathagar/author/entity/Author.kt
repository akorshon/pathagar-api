package com.marufh.pathagar.author.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.marufh.pathagar.book.entity.Book
import com.marufh.pathagar.base.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table

@Entity
@Table(name = "author")
class Author(
    @Column(name = "name", unique = true, length = 512)
    var name: String,

    @Column(name = "deleted", columnDefinition = "tinyint(1)")
    var deleted: Boolean = false,

    @Column(name = "description", length = 2048)
    var description: String? = null,

    @Column(name = "thumbnailPath", length = 512)
    var thumbnailPath: String? = null,

    @Column(name = "image_path", length = 512)
    var imagePath: String? = null,

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "authors")
    var books: Set<Book>? = mutableSetOf(),

    ): BaseEntity()

