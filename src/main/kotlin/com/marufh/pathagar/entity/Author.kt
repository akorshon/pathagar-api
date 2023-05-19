package com.marufh.pathagar.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "author")
class Author(
    @Column(name = "name")
    var name: String,

    @Column(name = "deleted", columnDefinition = "tinyint(1)")
    var deleted: Boolean = false,

    @Column(name = "description", length = 2048)
    var description: String? = null,

    @Column(name = "thumbnail")
    var thumbnail: String? = null,

    @Column(name = "image")
    var image: String? = null,
): BaseEntity()
