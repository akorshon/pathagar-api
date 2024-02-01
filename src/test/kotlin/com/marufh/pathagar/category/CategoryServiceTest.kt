package com.marufh.pathagar.category

import com.marufh.pathagar.BaseTest
import com.marufh.pathagar.book.service.AuthorAction
import com.marufh.pathagar.category.dto.CategoryCreateRequest
import com.marufh.pathagar.category.model.Category
import com.marufh.pathagar.exception.NotFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.nio.file.Files
import java.nio.file.Path
import java.util.*


class CategoryServiceTest: BaseTest() {

    @BeforeEach
    fun setup() {
        bookRepository.deleteAll()
        categoryRepository.deleteAll()
        fileMetaRepository.deleteAll()
        Files.deleteIfExists(Path.of(fileProperties.category).resolve("test-category/test-category.jpg"))
        Files.deleteIfExists(Path.of(fileProperties.category).resolve("test-category/test-category.jpg"))
    }

    @Test
    fun `test create category`() {
        // Given
        val categoryDto = getCategoryDto()

        // When
        val category = categoryService.create(categoryDto)

        // Then
        assert(category.id != null)
        assert(category.name == categoryDto.name)
        assert(category.description == categoryDto.description)
        assert(category.imageFile?.path == "category/test-category/test-category.jpg")
        assert(category.thumbFile?.path == "category/test-category/test-category_thumb.jpg")
    }

    @Test
    fun `test create category exception`() {

        val categoryDto = getCategoryDto();
        categoryDto.file = null

        val exception = assertThrows<IllegalArgumentException> {
            categoryService.create(categoryDto)
        }
        assert(exception.message == "File is required")
    }

    @Test
    fun `test update category`() {
        // Given
        val category = categoryService.create(getCategoryDto())

        // When
        val updatedName = "Updated Category Name" + UUID.randomUUID().toString()
        val updatedDescription = "Updated Category Description"
        val updatedCategory = categoryService.update(CategoryCreateRequest(
            id = category.id,
            name = updatedName,
            description = updatedDescription,
        ))

        // Then
        assert(updatedCategory.id != null)
        assert(updatedCategory.name == updatedName)
        assert(updatedCategory.description == updatedDescription)
        assert(updatedCategory.imageFile?.path == "category/test-category/test-category.jpg")
        assert(updatedCategory.thumbFile?.path == "category/test-category/test-category_thumb.jpg")
    }

    @Test
    fun `test update category not found exception`() {

        val categoryDto = getCategoryDto();
        categoryDto.id = "id-not-exist"
        val exception = assertThrows<NotFoundException> {
            categoryService.update(categoryDto)
        }
        assert(exception.message == "Category not found with id: ${categoryDto.id}")
    }

    @Test
    fun `test category find by id`() {
        // Given
        val categoryDto = getCategoryDto();
        val category = categoryService.create(categoryDto)
        val categoryFound = categoryService.findById(category.id!!);

        // Then
        assert(categoryFound.id == category.id)
        assert(categoryFound.name == category.name)
        assert(categoryFound.description == category.description)
        assert(categoryFound.imageFile?.path == "category/test-category/test-category.jpg")
        assert(categoryFound.thumbFile?.path == "category/test-category/test-category_thumb.jpg")
    }

    @Test
    fun `test book find by id not found exception`() {
        val exception = assertThrows<NotFoundException> {
            categoryService.findById("id-not-exist")
        }
        assert(exception.message == "Category not found with id: id-not-exist")
    }

    @Test
    fun `test find all`() {
        // Given
        val book = bookService.create(getBookDto())
        categoryRepository.deleteAll();
        val categoryList = listOf(
            Category(name = "Test Category 1", description = "Test Description 1"),
            Category(name = "Test Category 2", description = "Test Description 2"),
            Category(name = "Test Category 3", description = "Test Description 3"),
            Category(name = "Test Category 4", description = "Test Description 4"),
            Category(name = "Test Category 5", description = "Test Description 5"),
        ). map { categoryRepository.save(it) }

        for(category in categoryList) {
          bookService.updateCategory(book.id!!, category.id!!, AuthorAction.ADD.action)
        }

        // When
        val result = categoryService.findAll("", PageRequest.of(0, 10))

        // Then
        assert(result is PageImpl)
        assert(result.totalPages == 1)
        assert(result.content.size == 5)
        //assert(result.content[0].books?.size == 1)
    }

    @Test
    fun `test soft delete category`() {
        // Given
        val category = categoryService.create(getCategoryDto());

        // When
        categoryService.delete(category.id!!)

        assert(Files.exists(Path.of(fileProperties.category).resolve("test-category/test-category.jpg")))
        assert(Files.exists(Path.of(fileProperties.category).resolve("test-category/test-category_thumb.jpg")))
        categoryRepository.findById(category.id!!).let {
            assert(it.isPresent)
            assert(it.get().deleted)
        }
    }

    @Test
    fun `test hard delete category`() {
        // Given
        val category = categoryService.create(getCategoryDto());

        // When
        categoryService.delete(category.id!!) // soft delete
        categoryService.delete(category.id!!) // hard delete

        assert(!Files.exists(Path.of(fileProperties.category).resolve("test-category/test-category.jpg")))
        assert(!Files.exists(Path.of(fileProperties.category).resolve("test-category/test-category_thumb.jpg")))
        assertThrows<NotFoundException> {
            categoryService.findById(category.id!!)
        }
    }

    @Test
    fun `test delete category not found exception`() {
        val exception = assertThrows<NotFoundException> {
            categoryService.delete("id-not-found")
        }
        assert(exception.message == "Category not found with id: id-not-found")
    }
}
