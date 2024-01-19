package com.marufh.pathagar.category

import com.marufh.pathagar.author.entity.Author
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface CategoryMapper {
    fun toDto(category: Category): CategoryDto
    fun toEntity(categoryDto: CategoryDto): Category
}
