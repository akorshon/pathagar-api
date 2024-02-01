package com.marufh.pathagar.author

import com.marufh.pathagar.author.dto.AuthorDto
import com.marufh.pathagar.author.service.AuthorService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(AuthorController.AUTHOR_API)
class AuthorController(val authorService: AuthorService) {

    companion object {
        const val AUTHOR_API = "/api/user/authors"
    }

    @GetMapping("/{id}")
    fun getAuthorDetails(@PathVariable id: String) = ResponseEntity.ok(authorService.getAuthorDetails(id))

    @GetMapping
    fun getAll(@RequestParam(required = false) search: String?, pageable: Pageable):Page<AuthorDto>  =
        authorService.findAll(search, pageable)

}
