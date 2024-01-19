package com.marufh.pathagar.category

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/admin/categories")
class AdminCategoryController(
    private val categoryService: CategoryService) {

    @PostMapping
    fun create(@RequestBody categoryDto: CategoryDto): CategoryDto {
        return categoryService.create(categoryDto)
    }

    @PutMapping
    fun update(@RequestBody categoryDto: CategoryDto) = categoryService.update(categoryDto)

    @GetMapping("/{id}")
    fun getAuthorDetails(@PathVariable id: String) = categoryService.getDetails(id)

    @GetMapping()
    fun getAll(@RequestParam(required = false) search: String?, pageable: Pageable): Page<CategoryDto> {
        return categoryService.findAll(search, pageable)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String) = categoryService.delete(id)
}
