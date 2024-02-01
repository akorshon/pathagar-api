package com.marufh.pathagar.author

import com.marufh.pathagar.author.dto.AuthorCreateRequest
import com.marufh.pathagar.author.dto.AuthorDetailsResponse
import com.marufh.pathagar.author.dto.AuthorResponse
import com.marufh.pathagar.author.service.AuthorService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/admin/authors")
class AuthorAdminController(
    private val authorService: AuthorService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@ModelAttribute authorCreateRequest: AuthorCreateRequest): AuthorResponse =
        authorService.create(authorCreateRequest)

    @PutMapping
    fun update(@ModelAttribute authorCreateRequest: AuthorCreateRequest): AuthorResponse =
        authorService.update(authorCreateRequest)

    @GetMapping("/{id}")
    fun getAuthorDetails(@PathVariable id: String): AuthorDetailsResponse = authorService.getAuthorDetails(id)

    @GetMapping
    fun getAll(@RequestParam(required = false) search: String?, pageable: Pageable): Page<AuthorResponse> =
        authorService.findAll(search, pageable)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: String) = authorService.delete(id)
}
