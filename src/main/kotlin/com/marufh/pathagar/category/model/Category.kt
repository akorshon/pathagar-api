package com.marufh.pathagar.category.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.marufh.pathagar.base.entity.BaseEntity
import com.marufh.pathagar.book.entity.Book
import com.marufh.pathagar.file.entity.FileMeta
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

    @ManyToOne
    @JoinColumn(name = "image_file_id")
    var imageFile: FileMeta? = null,

    @ManyToOne
    @JoinColumn(name = "thumb_file_id")
    var thumbFile: FileMeta? = null,

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "authors")
    var books: Set<Book>? = mutableSetOf(),

    ): BaseEntity()
