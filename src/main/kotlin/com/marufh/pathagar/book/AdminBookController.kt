package com.marufh.pathagar.book

import com.marufh.pathagar.book.dto.BookDto
import com.marufh.pathagar.book.entity.Book
import com.marufh.pathagar.book.service.BookService
import com.marufh.pathagar.file.service.FileUploadService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/admin/books")
class AdminBookController(
    private val fileUploadService: FileUploadService,
    private val bookService: BookService) {

    @PostMapping
    fun create(@RequestBody bookDto: BookDto) = ResponseEntity(bookService.create(bookDto), HttpStatus.CREATED);

    @PutMapping
    fun update(@RequestBody bookDto: BookDto) = ResponseEntity.ok(bookService.update(bookDto));

    @GetMapping
    fun getAll(authentication: Authentication, @RequestParam(required = false) search: String?, pageable: Pageable) : ResponseEntity<Page<Book>> {
        return ResponseEntity.ok(bookService.findAll(search, pageable));
    }

    @PostMapping("{bookId}/authors/{authorId}/action/{action}")
    fun updateAuthor(@PathVariable bookId: String, @PathVariable authorId: String, @PathVariable action: String)  = ResponseEntity
        .ok(bookService.updateAuthor(bookId, authorId, action))


    @PostMapping("{bookId}/thumb/{page}")
    fun updateThumb(@PathVariable bookId: String, @PathVariable page: Int): Book = fileUploadService.updateThumb(bookId, page)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String) = bookService.delete(id)
}
