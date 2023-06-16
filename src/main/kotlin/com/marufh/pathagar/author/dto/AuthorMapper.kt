package com.marufh.pathagar.author.dto

import com.marufh.pathagar.author.entity.Author
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface AuthorMapper {
    fun toDto(author: Author): AuthorDto
    fun toEntity(authorDto: AuthorDto): Author
}
