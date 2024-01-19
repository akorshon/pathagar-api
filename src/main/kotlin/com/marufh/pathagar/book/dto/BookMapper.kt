package com.marufh.pathagar.book.dto

import com.marufh.pathagar.author.dto.AuthorMapper
import com.marufh.pathagar.book.entity.Book
import com.marufh.pathagar.category.CategoryMapper
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring")
interface BookMapper {

    fun toDto(book: Book): BookDto

    fun toEntity(bookDto: BookDto): Book
}
