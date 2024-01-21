package com.marufh.pathagar.category

import com.marufh.pathagar.category.dto.CategoryDto
import com.marufh.pathagar.category.service.CategoryService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/admin/categories")
class AdminCategoryController(
    private val categoryService: CategoryService
) {

    @PostMapping
    fun create(@ModelAttribute categoryDto: CategoryDto): CategoryDto {
        return categoryService.create(categoryDto)
    }

    @PutMapping
    fun update(@ModelAttribute categoryDto: CategoryDto) = categoryService.update(categoryDto)

    @GetMapping("/{id}")
    fun getAuthorDetails(@PathVariable id: String) = categoryService.getDetails(id)

    @GetMapping()
    fun getAll(@RequestParam(required = false) search: String?, pageable: Pageable): Page<CategoryDto> {
        return categoryService.findAll(search, pageable)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String) = categoryService.delete(id)
}
