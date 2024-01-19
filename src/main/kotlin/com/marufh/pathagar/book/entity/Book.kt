package com.marufh.pathagar.book.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.marufh.pathagar.author.entity.Author
import com.marufh.pathagar.base.entity.BaseEntity
import com.marufh.pathagar.category.Category
import com.marufh.pathagar.file.entity.FileType
import jakarta.persistence.*


@Entity
@Table(name = "book")
class Book(

    @Column(name = "name", length = 512, unique = true)
    var name: String,

    @Column(name = "description", length = 2048)
    var description: String? = null,

    @Column(name = "file_type")
    @Enumerated(EnumType.STRING)
    var fileType: FileType? = null,

    @Column(name = "file_path", length = 512)
    var filePath: String? = null,

    @Column(name = "cover_image", length = 512)
    var coverImage: String? = null,

    @Column(name = "cover_image_page", length = 512)
    var coverImagePage: Int? = null,

    @Column(name = "hash")
    var hash: String? = null,

    @Column(name = "size")
    var size: Long? = null,

    @Column(name = "total_page")
    var totalPage: Int? = null,

    @Column(name = "deleted", columnDefinition = "tinyint(1)")
    var deleted: Boolean? = false,

    @ManyToMany(fetch = FetchType.LAZY)
    var authors: MutableSet<Author>? = mutableSetOf(),

    @ManyToMany(fetch = FetchType.LAZY)
    var categories: MutableSet<Category>? = mutableSetOf(),

): BaseEntity()
