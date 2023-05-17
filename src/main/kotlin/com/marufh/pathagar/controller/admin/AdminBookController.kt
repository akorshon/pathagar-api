package com.marufh.pathagar.controller.admin

import com.marufh.pathagar.dto.BookDto
import com.marufh.pathagar.entity.Book
import com.marufh.pathagar.service.BookService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin/books")
class AdminBookController(
    private val bookService: BookService) {

    @PostMapping
    fun create(@ModelAttribute bookDto: BookDto): ResponseEntity<Book> {
        return ResponseEntity.ok(bookService.create(bookDto));
    }

    @GetMapping
    fun getAll(): ResponseEntity<List<Book>> {
        return ResponseEntity.ok(bookService.findAll());
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String): ResponseEntity<Void> {
        bookService.delete(id)
        return ResponseEntity.ok().build();
    }
}
