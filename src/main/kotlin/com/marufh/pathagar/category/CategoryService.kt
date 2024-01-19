package com.marufh.pathagar.category

import com.marufh.pathagar.book.dto.BookMapper
import com.marufh.pathagar.book.repository.BookRepository
import com.marufh.pathagar.config.FileProperties
import com.marufh.pathagar.exception.NotFoundException
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
    private val bookRepository: BookRepository,
    private val fileProperties: FileProperties,
    private val categoryMapper: CategoryMapper,
    private val bookMapper: BookMapper,
    private val categoryRepository: CategoryRepository) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun create(categoryDto: CategoryDto): CategoryDto {
        logger.info("Creating author: ${categoryDto.name}")

        categoryMapper.toEntity(categoryDto).run {
            return categoryMapper.toDto(categoryRepository.save(this))
        }
    }

    @Transactional
    fun update(categoryDto: CategoryDto): CategoryDto {
        logger.info("Updating author {}:", categoryDto.name)

        val author = categoryRepository.findById(categoryDto.id!!)
            .orElseThrow { EntityNotFoundException("Author not found with id: ${categoryDto.id}") }
        author.name = categoryDto.name
        author.description = categoryDto.description
        return categoryRepository.save(author).run { categoryMapper.toDto(this) }
    }

    @Transactional
    fun findById(id: String): CategoryDto {
        logger.info("Finding author by id: $id")

        return categoryRepository.findById(id)
            .map {categoryMapper.toDto(it) }
            .orElseThrow { NotFoundException("Author not found with id: $id") }
    }

    @Transactional
    fun getDetails(id: String): CategoryDto {
        logger.info("Getting author details $id")

        return findById(id).apply {
            books = bookRepository.findByCategoryId(id)
                .map { bookMapper.toDto(it) }
                .toList()
        }
    }

    @Transactional
    fun findAll(search: String?,  pageable: Pageable): Page<CategoryDto> {
        logger.info("Finding all categories")

        return categoryRepository.findAll(search, pageable)
            .map { categoryMapper.toDto(it) }
    }

    fun delete(id: String) {
        logger.info("Deleting author by id: $id")

        val author =  categoryRepository.findById(id)
            .orElseThrow{ NotFoundException("Author not found with id: $id") }

        try {
            logger.info("Deleting author image and thumbnail")
            Files.delete(Path.of(fileProperties.base +"/"+ author.imagePath))
            Files.delete(Path.of(fileProperties.base +"/"+ author.thumbnailPath))
            Files.delete(Path.of(fileProperties.base +"/"+ author.imagePath).parent)
        } catch (e: Exception) {
            logger.error("Error deleting file: {}", e.message)
        } finally {
            logger.info("Deleting author from database")
            categoryRepository.delete(author);
        }
    }

}
