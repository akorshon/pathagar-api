package com.marufh.pathagar.category.dto

import com.marufh.pathagar.file.entity.FileMeta

data class CategoryResponse(
    var id: String? = null,
    var name: String,
    var description: String? = null,
    var imageFile: FileMeta? = null,
    var thumbFile: FileMeta? = null,
)
