package com.marufh.pathagar.book.dto

import com.marufh.pathagar.book.entity.Book
import com.marufh.pathagar.file.dto.FileMetaMapper
import org.mapstruct.Mapper

@Mapper(componentModel = "spring", uses = [FileMetaMapper::class])
interface BookMapper {
    fun toDto(book: Book): BookDto
    fun toWithoutCategoryDto(book: Book): BookWithoutCategoryDto
    fun toEntity(bookDto: BookDto): Book
}
