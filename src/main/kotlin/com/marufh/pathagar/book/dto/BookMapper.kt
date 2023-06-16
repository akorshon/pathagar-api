package com.marufh.pathagar.book.dto

import com.marufh.pathagar.book.entity.Book
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface BookMapper {
    fun toDto(book: Book): BookDto
    fun toEntity(bookDto: BookDto): Book
}
