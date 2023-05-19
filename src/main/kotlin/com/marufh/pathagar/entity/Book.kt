package com.marufh.pathagar.entity

import jakarta.persistence.*


@Entity
@Table(name = "book")
class Book(

    @Column(name = "name")
    var name: String,

    @Column(name = "deleted", columnDefinition = "tinyint(1)")
    var deleted: Boolean = false,

    @Column(name = "description", length = 2048)
    var description: String? = null,

    @Column(name = "cover_image")
    var coverImage: String? = null,

    @Column(name = "file_type")
    @Enumerated(EnumType.STRING)
    var fileType: FileType? = null,

    @Column(name = "file_path")
    var filePath: String? = null

): BaseEntity()
