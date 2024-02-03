package com.marufh.pathagar.file.dto

import com.marufh.pathagar.file.dto.FileMetaResponse
import com.marufh.pathagar.file.entity.FileMeta

fun FileMeta.toFileMetaResponse() = FileMetaResponse(
    id = id,
    name = name,
    path = path,
    fileType = fileType,
    hash = hash,
    size = size,
    createdAt = createdAt
)