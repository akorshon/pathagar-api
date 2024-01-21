package com.marufh.pathagar.category.dto

import com.marufh.pathagar.category.model.Category
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface CategoryMapper {
    fun toDto(category: Category): CategoryDto
    fun toEntity(categoryDto: CategoryDto): Category
}
