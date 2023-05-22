package com.marufh.pathagar.controller.admin

import com.marufh.pathagar.dto.BookDto
import com.marufh.pathagar.service.BookService
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/admin/books")
class AdminBookController(
    private val bookService: BookService) {

    @PostMapping
    fun create(@RequestBody bookDto: BookDto) = ResponseEntity(bookService.create(bookDto), HttpStatus.CREATED);

    @PutMapping
    fun update(@ModelAttribute bookDto: BookDto) = ResponseEntity.ok(bookService.update(bookDto));

    @GetMapping
    fun getAll(@RequestParam(required = false) search: String?, pageable: Pageable) = ResponseEntity.ok(bookService.findAll(search, pageable));

    @PostMapping("{bookId}/authors/{authorId}/action/{action}")
    fun updateAuthor(@PathVariable bookId: String, @PathVariable authorId: String, @PathVariable action: String)  = ResponseEntity
        .ok(bookService.updateAuthor(bookId, authorId, action))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String) = bookService.delete(id)
}
