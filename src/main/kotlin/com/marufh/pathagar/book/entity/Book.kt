package com.marufh.pathagar.book.entity

import com.marufh.pathagar.author.entity.Author
import com.marufh.pathagar.base.entity.BaseEntity
import com.marufh.pathagar.category.model.Category
import com.marufh.pathagar.file.entity.FileMeta
import jakarta.persistence.*


@Entity
@Table(name = "book")
class Book(

    @Column(name = "name", length = 512, unique = true)
    var name: String,

    @Column(name = "description", length = 2048)
    var description: String? = null,

    @ManyToOne
    @JoinColumn(name = "pdf_file_id")
    var pdfFile: FileMeta? = null,

    @ManyToOne
    @JoinColumn(name = "cover_image_id")
    var coverImage: FileMeta? = null,

    @Column(name = "total_page")
    var totalPage: Int? = null,

    @Column(name = "deleted", columnDefinition = "tinyint(1)")
    var deleted: Boolean? = false,

    @ManyToMany(fetch = FetchType.LAZY)
    var authors: MutableSet<Author>? = mutableSetOf(),

    @ManyToMany(fetch = FetchType.LAZY)
    var categories: MutableSet<Category>? = mutableSetOf(),

    ): BaseEntity()
