package com.marufh.pathagar.controller.admin

import com.marufh.pathagar.dto.BookDto
import com.marufh.pathagar.service.BookService
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/admin/books")
class AdminBookController(
    private val bookService: BookService) {

    @PostMapping
    fun create(@ModelAttribute bookDto: BookDto) = ResponseEntity.ok(bookService.create(bookDto));

    @GetMapping
    fun getAll(pageable: Pageable) = ResponseEntity.ok(bookService.findAll(pageable));

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String) = bookService.delete(id)
}
