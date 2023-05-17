package com.marufh.pathagar.controller

import com.marufh.pathagar.dto.AuthorDto
import com.marufh.pathagar.entity.Author
import com.marufh.pathagar.service.AuthorService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/public/authors")
class AuthorController(
    private val authorService: AuthorService) {

    @PostMapping
    fun create(@RequestBody authorDto: AuthorDto): ResponseEntity<Author> {
        return ResponseEntity
            .ok(authorService.create(authorDto));
    }

    @PutMapping
    fun update(@RequestBody authorDto: AuthorDto): ResponseEntity<Author> {
        return ResponseEntity
            .ok(authorService.update(authorDto));
    }

    @GetMapping
    fun getAll(): ResponseEntity<List<Author>> {
        return ResponseEntity
            .ok(authorService.findAll());
    }
}
