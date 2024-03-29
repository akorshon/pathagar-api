package com.marufh.pathagar.category

import com.marufh.pathagar.category.dto.CategoryDetailsResponse
import com.marufh.pathagar.category.dto.CategoryResponse
import com.marufh.pathagar.category.service.CategoryService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user/categories")
class CategoryController(val categoryService: CategoryService) {

    @GetMapping("/{id}")
    fun getAuthorDetails(@PathVariable id: String): CategoryDetailsResponse = categoryService.getDetails(id)

    @GetMapping
    fun getAll(@RequestParam(required = false) search: String?, pageable: Pageable): Page<CategoryResponse> =
        categoryService.findAll(search, pageable)

    @GetMapping("/details")
    fun getAllDetails(@RequestParam(required = false) search: String?, pageable: Pageable): Page<CategoryDetailsResponse> =
        categoryService.findAllDetails(search, pageable)

}
