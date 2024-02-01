package com.marufh.pathagar.category.service

import com.marufh.pathagar.book.repository.BookRepository
import com.marufh.pathagar.category.dto.*
import com.marufh.pathagar.category.model.Category
import com.marufh.pathagar.category.model.CategoryRepository
import com.marufh.pathagar.config.FileProperties
import com.marufh.pathagar.exception.NotFoundException
import com.marufh.pathagar.extension.toBookResponse
import com.marufh.pathagar.extension.toCategoryDetailsResponse
import com.marufh.pathagar.extension.toCategoryResponse
import com.marufh.pathagar.file.dto.FileDto
import com.marufh.pathagar.file.entity.FileType
import com.marufh.pathagar.file.service.FileService
import com.marufh.pathagar.file.service.FileUploadService
import com.marufh.pathagar.file.service.ImageResizeService
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.nio.file.Path

@Service
class CategoryService(
    private val fileService: FileService,
    private val imageResizeService: ImageResizeService,
    private val fileUploadService: FileUploadService,
    private val bookRepository: BookRepository,
    private val fileProperties: FileProperties,
    private val categoryRepository: CategoryRepository) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun create(categoryCreateRequest: CategoryCreateRequest): CategoryResponse {
        logger.info("Creating author: ${categoryCreateRequest.name}")

        requireNotNull(categoryCreateRequest.file) {
            throw IllegalArgumentException("File is required")
        }

        val imageFile = fileUploadService.createFile(FileDto(
            name = categoryCreateRequest.file?.name!!,
            fileType = FileType.CATEGORY,
            file = categoryCreateRequest.file!!
        ))
        val thumbFile = imageResizeService.createThumb(Path.of(fileProperties.base, imageFile.path), FileType.CATEGORY_THUMB)

        return Category(
            name = categoryCreateRequest.name,
            description = categoryCreateRequest.description,
            imageFile = imageFile,
            thumbFile = thumbFile
        ).let {
            categoryRepository.save(it).toCategoryResponse()
        }
    }

    @Transactional
    fun update(categoryCreateRequest: CategoryCreateRequest): CategoryResponse {
        logger.info("Updating author {}:", categoryCreateRequest.name)

        val author = categoryRepository.findById(categoryCreateRequest.id!!)
            .orElseThrow { NotFoundException("Category not found with id: ${categoryCreateRequest.id}") }
        author.name = categoryCreateRequest.name
        author.description = categoryCreateRequest.description
        if (categoryCreateRequest.file != null) {
            val imageFile = fileUploadService.createFile(FileDto(
                name = categoryCreateRequest.file?.name!!,
                fileType = FileType.CATEGORY,
                file = categoryCreateRequest.file!!
            ))
            val thumbFile = imageResizeService.createThumb(Path.of(fileProperties.base, imageFile.path), FileType.CATEGORY_THUMB)
            author.imageFile = imageFile
            author.thumbFile = thumbFile
        }

        return categoryRepository.save(author).toCategoryResponse()
    }

    @Transactional
    fun findById(id: String): CategoryDetailsResponse {
        logger.info("Finding author by id: $id")

        return categoryRepository.findByIdOrNull(id)?.toCategoryDetailsResponse()
            ?: throw NotFoundException("Category not found with id: $id")
    }

    @Transactional
    fun getDetails(id: String): CategoryDetailsResponse {
        logger.info("Getting author details $id")

        return findById(id).apply {
            books = bookRepository.findByCategoryId(id)
                .map { it.toBookResponse() }
                .toSet()
        }
    }

    @Transactional
    fun findAll(search: String?,  pageable: Pageable): Page<CategoryResponse> {
        logger.info("Finding all categories")

        return categoryRepository.findAll(search, pageable)
            .map { it.toCategoryResponse() }
    }

    @Transactional
    fun findAllDetails(search: String?,  pageable: Pageable): Page<CategoryDetailsResponse> {
        logger.info("Finding all categories")

        val listOfCategory = categoryRepository.findAll(search, pageable).content
            .filter { it.books?.isNotEmpty()!! }
            .map { it.toCategoryDetailsResponse() }

        return PageImpl(listOfCategory, pageable, listOfCategory.size.toLong())
    }

    fun delete(id: String) {
        logger.info("Deleting category by id: $id")

        val category =  categoryRepository.findById(id)
            .orElseThrow{ NotFoundException("Category not found with id: $id") }

        if(category.deleted) {
            fileService.deleteFiles(category.imageFile?.path!!, category.thumbFile?.path!!)
            categoryRepository.delete(category)
        } else {
            category.deleted = true
            categoryRepository.save(category)
        }
    }

}
