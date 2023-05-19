package com.marufh.pathagar.controller.admin

import com.marufh.pathagar.dto.AuthorDto
import com.marufh.pathagar.service.AuthorService
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/admin/authors")
class AdminAuthorController(
    private val authorService: AuthorService) {

    @PostMapping
    fun create(@ModelAttribute authorDto: AuthorDto) = ResponseEntity.ok(authorService.create(authorDto))

    @PutMapping
    fun update(@RequestBody authorDto: AuthorDto) = ResponseEntity.ok(authorService.update(authorDto));

    @GetMapping()
    fun getAll(pageable: Pageable) = ResponseEntity.ok(authorService.findAll(pageable));

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String) = authorService.delete(id)
}
