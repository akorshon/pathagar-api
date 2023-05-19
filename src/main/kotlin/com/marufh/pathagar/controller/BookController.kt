package com.marufh.pathagar.controller

import com.marufh.pathagar.entity.Book
import com.marufh.pathagar.service.BookService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/public/books")
class BookController(
    private val bookService: BookService) {

    @GetMapping
    fun getAll(pageable: Pageable): ResponseEntity<Page<Book>> {
        return ResponseEntity.ok(bookService.findAll(pageable));
    }
}
