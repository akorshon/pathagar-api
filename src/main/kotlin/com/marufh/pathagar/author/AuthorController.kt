package com.marufh.pathagar.author

import com.marufh.pathagar.author.dto.AuthorDetailsResponse
import com.marufh.pathagar.author.dto.AuthorResponse
import com.marufh.pathagar.author.service.AuthorService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*

const val AUTHOR_URL = "/api/user/authors"

@RestController
@RequestMapping(AUTHOR_URL)
class AuthorController(
    val authorService: AuthorService) {

    @GetMapping("/{id}")
    fun getAuthorDetails(@PathVariable id: String): AuthorDetailsResponse = authorService.getAuthorDetails(id)

    @GetMapping
    fun getAll(@RequestParam(required = false) search: String?, pageable: Pageable):Page<AuthorResponse>  =
        authorService.findAll(search, pageable)
}
