package com.marufh.pathagar.book

import com.marufh.pathagar.book.dto.BookDetailsResponse
import com.marufh.pathagar.book.dto.BookResponse
import com.marufh.pathagar.book.service.BookService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user/books")
class BookController(val bookService: BookService) {

    @GetMapping("/{id}")
    fun findById(@PathVariable id: String): BookDetailsResponse = bookService.findById(id)

    @GetMapping
    fun getAll(@RequestParam(required = false) search: String?,
               pageable: Pageable,
               authentication: Authentication): Page<BookResponse> = bookService.findAll(search, pageable)
}
