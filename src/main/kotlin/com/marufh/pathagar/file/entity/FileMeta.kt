package com.marufh.pathagar.file.entity

import com.marufh.pathagar.base.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "file_meta")
class FileMeta(

    @Column(name = "name")
    var name: String,

    @Column(name = "path")
    var path: String,

    @Column(name = "file_type")
    @Enumerated(EnumType.STRING)
    var fileType: FileType,

    @Column(name = "hash")
    var hash: String,

    @Column(name = "size")
    var size: Long,

    @Column(name = "created_at")
    var createdAt: Instant,

    ): BaseEntity()