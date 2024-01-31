package com.marufh.pathagar.book

import com.marufh.pathagar.book.dto.BookDto
import com.marufh.pathagar.book.service.BookService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user/books")
class BookController(val bookService: BookService) {

    @GetMapping
    fun getAll(@RequestParam(required = false) search: String?, pageable: Pageable, authentication: Authentication): Page<BookDto> {
        return bookService.findAll(search, pageable)
    }

}
