package com.marufh.pathagar.book

import com.marufh.pathagar.auth.config.toUser
import com.marufh.pathagar.book.dto.UserBookDto
import com.marufh.pathagar.book.service.UserBookService
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user/user-books")
class UserBookController(val userBookService: UserBookService) {

    @PostMapping
    fun create(@RequestBody userBookDto: UserBookDto, authentication: Authentication) = userBookService.create(authentication.toUser(), userBookDto)

    @PutMapping
    fun update(@RequestBody userBookDto: UserBookDto, authentication: Authentication) = ResponseEntity.ok(userBookService.update(authentication.toUser(), userBookDto))

    @GetMapping("/book/{bookId}")
    fun getByUserAndBookId(@PathVariable bookId: String, authentication: Authentication) = userBookService.findByUserAndBookId(authentication.toUser(), bookId)

    @GetMapping
    fun getAll(authentication: Authentication, pageable: Pageable) = userBookService.findAll(authentication.toUser(), pageable)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String, authentication: Authentication) = userBookService.delete(authentication.toUser(), id)
}
