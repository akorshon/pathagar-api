package com.marufh.pathagar.category

import com.marufh.pathagar.category.dto.CategoryDto
import com.marufh.pathagar.category.service.CategoryService
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user/categories")
class CategoryController(val categoryService: CategoryService) {

    @GetMapping("/{id}")
    fun getAuthorDetails(@PathVariable id: String): CategoryDto = categoryService.getDetails(id)

    @GetMapping
    fun getAll(@RequestParam(required = false) search: String?, pageable: Pageable) = ResponseEntity
        .ok(categoryService.findAll(search, pageable))

}
