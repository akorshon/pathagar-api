package com.marufh.pathagar.author.dto

import com.marufh.pathagar.author.entity.Author
import com.marufh.pathagar.book.dto.BookMapper
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring", uses = [BookMapper::class])
interface AuthorMapper {

    fun toDto(author: Author): AuthorDto

    fun toDetailsDto(author: Author): AuthorDetailsDto

    @Mapping(target = "books", ignore = true)
    fun toEntity(authorDto: AuthorDto): Author
}
