package com.marufh.pathagar.book

import com.marufh.pathagar.auth.config.toUser
import com.marufh.pathagar.book.dto.UserBookDto
import com.marufh.pathagar.book.entity.UserBook
import com.marufh.pathagar.book.service.UserBookService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user/user-books")
class UserBookController(val userBookService: UserBookService) {

    @PutMapping
    fun update(@RequestBody userBookDto: UserBookDto, authentication: Authentication): UserBook {
        return userBookService.update(authentication.toUser(), userBookDto)
    }

    @GetMapping("/book/{bookId}")
    fun getByUserAndBookId(@PathVariable bookId: String, authentication: Authentication): UserBook {
        return userBookService.findByUserAndBookId(authentication.toUser(), bookId)
    }

    @GetMapping
    fun getAll(authentication: Authentication, pageable: Pageable): Page<UserBook> {
        return userBookService.findAll(authentication.toUser(), pageable)
    }

    @GetMapping("/status/{status}")
    fun getAll(authentication: Authentication, @PathVariable status: String, pageable: Pageable): Page<UserBook> {
        return userBookService.findAllByStatus(authentication.toUser(), status, pageable)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String, authentication: Authentication) {
        userBookService.delete(authentication.toUser(), id)
    }
}
