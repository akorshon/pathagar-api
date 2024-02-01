package com.marufh.pathagar.category.dto

import com.marufh.pathagar.file.dto.FileMetaResponse

data class CategoryResponse(
    var id: String? = null,
    var name: String,
    var description: String? = null,
    var imageFile: FileMetaResponse? = null,
    var thumbFile: FileMetaResponse? = null,
)
