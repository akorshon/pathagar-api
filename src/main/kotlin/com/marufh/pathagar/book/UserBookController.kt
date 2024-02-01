package com.marufh.pathagar.book

import com.marufh.pathagar.auth.config.toUser
import com.marufh.pathagar.book.dto.UserBookRequest
import com.marufh.pathagar.book.dto.UserBookResponse
import com.marufh.pathagar.book.service.UserBookService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user/user-books")
class UserBookController(val userBookService: UserBookService) {

    @PutMapping
    fun update(@RequestBody userBookRequest: UserBookRequest,
               authentication: Authentication): UserBookResponse =
        userBookService.update(authentication.toUser(), userBookRequest)

    @GetMapping("/book/{bookId}")
    fun getByUserAndBookId(@PathVariable bookId: String,
                           authentication: Authentication): UserBookResponse =
        userBookService.findByUserAndBookId(authentication.toUser(), bookId)

    @GetMapping
    fun getAll(authentication: Authentication, pageable: Pageable): Page<UserBookResponse> =
        userBookService.findAll(authentication.toUser(), pageable)

    @GetMapping("/status/{status}")
    fun getAll(authentication: Authentication,
               @PathVariable status: String,
               pageable: Pageable): Page<UserBookResponse> =
        userBookService.findAllByStatus(authentication.toUser(), status, pageable)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String, authentication: Authentication) =
        userBookService.delete(authentication.toUser(), id)
}
