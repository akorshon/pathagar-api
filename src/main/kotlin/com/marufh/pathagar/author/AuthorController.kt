package com.marufh.pathagar.author

import com.marufh.pathagar.author.service.AuthorService
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user/authors")
class AuthorController(val authorService: AuthorService) {

    @GetMapping("/{id}")
    fun getAuthorDetails(@PathVariable id: String) = ResponseEntity.ok(authorService.getAuthorDetails(id))

    @GetMapping
    fun getAll(@RequestParam(required = false) search: String?, pageable: Pageable) = ResponseEntity
        .ok(authorService.findAll(search, pageable));

}
