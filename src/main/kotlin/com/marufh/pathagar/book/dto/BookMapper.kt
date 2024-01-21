package com.marufh.pathagar.book.dto

import com.marufh.pathagar.book.entity.Book
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface BookMapper {

    fun toDto(book: Book): BookDto

    @Mapping(target = "authors", ignore = true)
    fun toEntity(bookDto: BookDto): Book
}
