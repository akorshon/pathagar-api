package com.marufh.pathagar.file.dto

import com.marufh.pathagar.file.entity.FileType
import java.time.Instant

data class FileMetaResponse(
    var id: String? = null,
    var name: String,
    var path: String,
    var fileType: FileType,
    var hash: String,
    var size: Long,
    var createdAt: Instant
)