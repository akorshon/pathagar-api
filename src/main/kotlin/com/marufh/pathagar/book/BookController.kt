package com.marufh.pathagar.book

import com.marufh.pathagar.book.service.BookService
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user/books")
class BookController(val bookService: BookService) {

    @GetMapping
    fun getAll(@RequestParam(required = false) search: String?, pageable: Pageable, authentication: Authentication) =
        ResponseEntity.ok(bookService.findAll(search, pageable))
}
