package com.marufh.pathagar.author

import com.marufh.pathagar.author.dto.AuthorDetailsDto
import com.marufh.pathagar.author.dto.AuthorDto
import com.marufh.pathagar.author.service.AuthorService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/admin/authors")
class AuthorAdminController(
    private val authorService: AuthorService) {

    @PostMapping
    fun create(@ModelAttribute authorDto: AuthorDto): AuthorDto {
        return authorService.create(authorDto)
    }

    @PutMapping
    fun update(@ModelAttribute authorDto: AuthorDto): AuthorDto {
        return authorService.update(authorDto)
    }

    @GetMapping("/{id}")
    fun getAuthorDetails(@PathVariable id: String): AuthorDetailsDto {
      return authorService.getAuthorDetails(id)
    }

    @GetMapping()
    fun getAll(@RequestParam(required = false) search: String?, pageable: Pageable): Page<AuthorDto> {
      return authorService.findAll(search, pageable)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String) = authorService.delete(id)
}
