package com.marufh.pathagar.controller.private

import com.marufh.pathagar.service.BookService
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/private/books")
class PrivateBookController(
    private val bookService: BookService) {

    @GetMapping
    fun getAll(pageable: Pageable) = bookService.findAll(pageable)
}
