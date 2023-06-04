package com.marufh.pathagar.author

import com.marufh.pathagar.author.dto.AuthorDto
import com.marufh.pathagar.author.service.AuthorService
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/admin/authors")
class AdminAuthorController(
    private val authorService: AuthorService) {

    @PostMapping
    fun create(@RequestBody authorDto: AuthorDto) = ResponseEntity.ok(authorService.create(authorDto))

    @PutMapping
    fun update(@RequestBody authorDto: AuthorDto) = ResponseEntity.ok(authorService.update(authorDto));

    @GetMapping("/{id}")
    fun getAuthorDetails(@PathVariable id: String) = ResponseEntity.ok(authorService.getAuthorDetails(id))

    @GetMapping()
    fun getAll(@RequestParam(required = false) search: String?, pageable: Pageable) = ResponseEntity
        .ok(authorService.findAll(search, pageable));

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String) = authorService.delete(id)
}
