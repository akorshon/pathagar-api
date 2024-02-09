package com.marufh.pathagar.book

import com.marufh.pathagar.book.dto.BookCreateRequest
import com.marufh.pathagar.book.dto.BookDetailsResponse
import com.marufh.pathagar.book.dto.BookResponse
import com.marufh.pathagar.book.service.BookService
import org.springframework.data.domain.Page
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
    fun create(@ModelAttribute bookCreateRequest: BookCreateRequest): BookResponse = bookService.create(bookCreateRequest)

    @PutMapping
    fun update(@ModelAttribute bookCreateRequest: BookCreateRequest): BookResponse = bookService.update(bookCreateRequest)

    @GetMapping("/{id}")
    fun findById(@PathVariable id: String): BookDetailsResponse = bookService.findById(id)

    @GetMapping
    fun getAll(authentication: Authentication,
               @RequestParam(required = false) search: String?, pageable: Pageable): Page<BookResponse> =
        bookService.findAll(search, pageable)

    @PostMapping("{bookId}/authors/{authorId}/action/{action}")
    fun updateAuthor(@PathVariable bookId: String,
                     @PathVariable authorId: String,
                     @PathVariable action: String): BookDetailsResponse  = bookService.updateAuthor(bookId, authorId, action)

    @PostMapping("{bookId}/categories/{categoryId}/action/{action}")
    fun updateCategory(@PathVariable bookId: String,
                       @PathVariable categoryId: String,
                       @PathVariable action: String): BookDetailsResponse = bookService.updateCategory(bookId, categoryId, action)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String) = bookService.delete(id)
}
