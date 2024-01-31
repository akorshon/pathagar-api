package com.marufh.pathagar.category.service

import com.marufh.pathagar.book.dto.BookMapper
import com.marufh.pathagar.book.repository.BookRepository
import com.marufh.pathagar.category.dto.CategoryDto
import com.marufh.pathagar.category.dto.CategoryMapper
import com.marufh.pathagar.category.model.CategoryRepository
import com.marufh.pathagar.config.FileProperties
import com.marufh.pathagar.exception.NotFoundException
import com.marufh.pathagar.file.dto.FileDto
import com.marufh.pathagar.file.entity.FileType
import com.marufh.pathagar.file.service.FileUploadService
import com.marufh.pathagar.file.service.ImageResizeService
import jakarta.persistence.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.nio.file.Files
import java.nio.file.Path

@Service
class CategoryService(
    private val imageResizeService: ImageResizeService,
    private val fileUploadService: FileUploadService,
    private val bookRepository: BookRepository,
    private val fileProperties: FileProperties,
    private val categoryMapper: CategoryMapper,
    private val bookMapper: BookMapper,
    private val categoryRepository: CategoryRepository) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun create(categoryDto: CategoryDto): CategoryDto {
        logger.info("Creating author: ${categoryDto.name}")

        requireNotNull(categoryDto.file) {
            throw IllegalArgumentException("File is required")
        }

        val imageFile = fileUploadService.createFile(FileDto(
            name = categoryDto.file?.name!!,
            fileType = FileType.CATEGORY,
            file = categoryDto.file!!
        ))
        val thumbFile = imageResizeService.createThumb(Path.of(fileProperties.base, imageFile.path), FileType.CATEGORY_THUMB)

        return categoryMapper.toEntity(categoryDto).let {
            it.imageFile = imageFile
            it.thumbFile = thumbFile
            categoryMapper.toDto(categoryRepository.save(it))
        }
    }

    @Transactional
    fun update(categoryDto: CategoryDto): CategoryDto {
        logger.info("Updating author {}:", categoryDto.name)

        val author = categoryRepository.findById(categoryDto.id!!)
            .orElseThrow { NotFoundException("Category not found with id: ${categoryDto.id}") }
        author.name = categoryDto.name
        author.description = categoryDto.description
        if (categoryDto.file != null) {
            val imageFile = fileUploadService.createFile(FileDto(
                name = categoryDto.file?.name!!,
                fileType = FileType.CATEGORY,
                file = categoryDto.file!!
            ))
            val thumbFile = imageResizeService.createThumb(Path.of(fileProperties.base, imageFile.path), FileType.CATEGORY_THUMB)
            author.imageFile = imageFile
            author.thumbFile = thumbFile
        }

        return categoryRepository.save(author).let {
            categoryMapper.toDto(it)
        }
    }

    @Transactional
    fun findById(id: String): CategoryDto {
        logger.info("Finding author by id: $id")

        return categoryRepository.findById(id)
            .map {categoryMapper.toDto(it) }
            .orElseThrow { NotFoundException("Category not found with id: $id") }
    }

    @Transactional
    fun getDetails(id: String): CategoryDto {
        logger.info("Getting author details $id")

        return findById(id).apply {
            books = bookRepository.findByCategoryId(id)
                .map { bookMapper.toWithoutCategoryDto(it) }
                .toSet()
        }
    }

    @Transactional
    fun findAll(search: String?,  pageable: Pageable): Page<CategoryDto> {
        logger.info("Finding all categories")

        val categoryPage =  categoryRepository.findAll(search, pageable)
            .map {
//                it.books = bookRepository.findByCategoryId(it.id!!)
                categoryMapper.toDto(it)
            }

        return categoryPage;


    }

    fun delete(id: String) {
        logger.info("Deleting category by id: $id")

        val category =  categoryRepository.findById(id)
            .orElseThrow{ NotFoundException("Category not found with id: $id") }

        if(category.deleted) {
            Files.delete(Path.of(fileProperties.base, category.imageFile?.path))
            Files.delete(Path.of(fileProperties.base, category.thumbFile?.path))
            Files.delete(Path.of(fileProperties.base, category.imageFile?.path).parent)
            categoryRepository.delete(category);
        } else {
            category.deleted = true
            categoryRepository.save(category)
        }
    }

}
