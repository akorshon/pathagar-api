package com.marufh.pathagar.author.service

import com.marufh.pathagar.author.dto.AuthorCreateRequest
import com.marufh.pathagar.author.dto.AuthorDetailsResponse
import com.marufh.pathagar.author.dto.AuthorResponse
import com.marufh.pathagar.author.entity.Author
import com.marufh.pathagar.author.repository.AuthorRepository
import com.marufh.pathagar.book.repository.BookRepository
import com.marufh.pathagar.config.FileProperties
import com.marufh.pathagar.exception.NotFoundException
import com.marufh.pathagar.author.dto.toAuthorDetailsResponse
import com.marufh.pathagar.author.dto.toAuthorResponse
import com.marufh.pathagar.book.dto.toBookResponse
import com.marufh.pathagar.file.dto.FileDto
import com.marufh.pathagar.file.entity.FileType
import com.marufh.pathagar.file.service.FileService
import com.marufh.pathagar.file.service.FileUploadService
import com.marufh.pathagar.file.service.ImageResizeService
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.nio.file.Path

@Service
class AuthorService(
    private val fileService: FileService,
    private val imageResizeService: ImageResizeService,
    private val fileUploadService: FileUploadService,
    private val bookRepository: BookRepository,
    private val fileProperties: FileProperties,
    private val authorRepository: AuthorRepository) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun create(authorCreateRequest: AuthorCreateRequest): AuthorResponse {
        logger.info("Creating author: ${authorCreateRequest.name}")

        requireNotNull(authorCreateRequest.file) {
            throw IllegalArgumentException("File is required")
        }

        val authorImage = fileUploadService.createFile(FileDto(
            name = authorCreateRequest.name,
            file = authorCreateRequest.file!!,
            fileType = FileType.AUTHOR
        ))
        val authorImageThumb = imageResizeService.createThumb(Path.of(fileProperties.base +"/"+ authorImage.path), FileType.AUTHOR_THUMB)

        return Author(
            name = authorCreateRequest.name,
            description = authorCreateRequest.description,
            imageFile = authorImage,
            thumbFile = authorImageThumb
        ).let {
            authorRepository.save(it).toAuthorResponse()
        }
    }

    @Transactional
    fun update(authorCreateRequest: AuthorCreateRequest): AuthorResponse {
        logger.info("Updating author: ${authorCreateRequest.name}")

        val author = authorRepository.findById(authorCreateRequest.id!!)
            .orElseThrow { NotFoundException("Author not found with id: ${authorCreateRequest.id}") }

        author.name = authorCreateRequest.name
        author.description = authorCreateRequest.description
        if (authorCreateRequest.file != null) {
            val authorImage = fileUploadService.createFile(FileDto(
                name = authorCreateRequest.name,
                file = authorCreateRequest.file!!,
                fileType = FileType.AUTHOR
            ))
            val authorImageThumb = imageResizeService.createThumb(Path.of(fileProperties.base +"/"+ authorImage.path), FileType.AUTHOR)
            author.imageFile = authorImage
            author.thumbFile = authorImageThumb
        }

        return authorRepository.save(author).toAuthorResponse()
    }

    @Transactional
    fun findById(id: String): AuthorDetailsResponse {
        logger.info("Finding author by id: $id")

        return authorRepository.findByIdOrNull(id)?.toAuthorDetailsResponse()
            ?: throw NotFoundException("Author not found with id: $id")
    }

    @Transactional
    fun getAuthorDetails(id: String): AuthorDetailsResponse {
        logger.info("Getting author details $id")

        return findById(id).apply {
            books = bookRepository.findByAuthorId(id)
                .map { it.toBookResponse() }
                .toList()
        }
    }

    @Transactional
    fun findAll(search: String?,  pageable: Pageable): Page<AuthorResponse> {
        logger.info("Finding all authors")

        return authorRepository.findAll(search, pageable)
            .map { it.toAuthorResponse() }
    }

    @Transactional
    fun delete(id: String) {
        logger.info("Deleting author by id: $id")

        val author =  authorRepository.findById(id)
            .orElseThrow{ NotFoundException("Author not found with id: $id") }

        if(author.deleted) {
            fileService.deleteFiles(author.imageFile?.path!!, author.thumbFile?.path!!)
            authorRepository.delete(author)
        } else {
            author.deleted = true
            authorRepository.save(author)
        }
    }

}
