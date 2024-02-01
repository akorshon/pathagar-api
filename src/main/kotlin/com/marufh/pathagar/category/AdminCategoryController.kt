package com.marufh.pathagar.category

import com.marufh.pathagar.category.dto.CategoryCreateRequest
import com.marufh.pathagar.category.dto.CategoryDetailsResponse
import com.marufh.pathagar.category.dto.CategoryDto
import com.marufh.pathagar.category.dto.CategoryResponse
import com.marufh.pathagar.category.service.CategoryService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/admin/categories")
class AdminCategoryController(
    private val categoryService: CategoryService) {

    @PostMapping
    fun create(@ModelAttribute categoryCreateRequest: CategoryCreateRequest): CategoryResponse =
        categoryService.create(categoryCreateRequest)

    @PutMapping
    fun update(@ModelAttribute categoryCreateRequest: CategoryCreateRequest): CategoryResponse =
        categoryService.update(categoryCreateRequest)

    @GetMapping("/{id}")
    fun getAuthorDetails(@PathVariable id: String): CategoryDetailsResponse = categoryService.getDetails(id)

    @GetMapping
    fun getAll(@RequestParam(required = false) search: String?, pageable: Pageable): Page<CategoryResponse> =
        categoryService.findAll(search, pageable)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: String) = categoryService.delete(id)
}
