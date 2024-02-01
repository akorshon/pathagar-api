package com.marufh.pathagar.book

import com.marufh.pathagar.book.dto.BookDto
import com.marufh.pathagar.book.service.BookService
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/admin/books")
class AdminBookController(
    private val bookService: BookService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@ModelAttribute bookDto: BookDto): BookDto = bookService.create(bookDto)

    @PutMapping
    fun update(@RequestBody bookDto: BookDto): BookDto = bookService.update(bookDto)

    @GetMapping("/{id}")
    fun findById(@PathVariable id: String): BookDto = bookService.findById(id)

    @GetMapping
    fun getAll(authentication: Authentication,
               @RequestParam(required = false) search: String?,
               pageable: Pageable) = bookService.findAll(search, pageable)

    @PostMapping("{bookId}/authors/{authorId}/action/{action}")
    fun updateAuthor(@PathVariable bookId: String,
                     @PathVariable authorId: String,
                     @PathVariable action: String)  = bookService.updateAuthor(bookId, authorId, action)

    @PostMapping("{bookId}/categories/{categoryId}/action/{action}")
    fun updateCategory(@PathVariable bookId: String,
                       @PathVariable categoryId: String,
                       @PathVariable action: String): BookDto = bookService.updateCategory(bookId, categoryId, action)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String) = bookService.delete(id)
}
