package com.marufh.pathagar.author.service

import com.marufh.pathagar.author.dto.AuthorDetailsDto
import com.marufh.pathagar.author.dto.AuthorDto
import com.marufh.pathagar.author.dto.AuthorMapper
import com.marufh.pathagar.author.repository.AuthorRepository
import com.marufh.pathagar.book.dto.BookMapper
import com.marufh.pathagar.book.repository.BookRepository
import com.marufh.pathagar.config.FileProperties
import com.marufh.pathagar.exception.NotFoundException
import com.marufh.pathagar.file.dto.FileDto
import com.marufh.pathagar.file.entity.FileType
import com.marufh.pathagar.file.service.FileService
import com.marufh.pathagar.file.service.FileUploadService
import com.marufh.pathagar.file.service.ImageResizeService
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.nio.file.Files
import java.nio.file.Path

@Service
class AuthorService(
    private val fileService: FileService,
    private val imageResizeService: ImageResizeService,
    private val fileUploadService: FileUploadService,
    private val bookRepository: BookRepository,
    private val fileProperties: FileProperties,
    private val authorMapper: AuthorMapper,
    private val bookMapper: BookMapper,
    private val authorRepository: AuthorRepository) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun create(authorDto: AuthorDto): AuthorDto {
        logger.info("Creating author: ${authorDto.name}")

        requireNotNull(authorDto.file) {
            throw IllegalArgumentException("File is required")
        }

        val authorImage = fileUploadService.createFile(FileDto(
            name = authorDto.name,
            file = authorDto.file!!,
            fileType = FileType.AUTHOR
        ))
        val authorImageThumb = imageResizeService.createThumb(Path.of(fileProperties.base +"/"+ authorImage.path), FileType.AUTHOR_THUMB)

        return authorMapper.toEntity(authorDto).let {
            it.imageFile = authorImage
            it.thumbFile = authorImageThumb
            authorMapper.toDto(authorRepository.save(it))
        }
    }

    @Transactional
    fun update(authorDto: AuthorDto): AuthorDto {
        logger.info("Updating author: ${authorDto.name}")

        val author = authorRepository.findById(authorDto.id!!)
            .orElseThrow { NotFoundException("Author not found with id: ${authorDto.id}") }

        author.name = authorDto.name
        author.description = authorDto.description
        if (authorDto.file != null) {
            val authorImage = fileUploadService.createFile(FileDto(
                name = authorDto.name,
                file = authorDto.file!!,
                fileType = FileType.AUTHOR
            ))
            val authorImageThumb = imageResizeService.createThumb(Path.of(fileProperties.base +"/"+ authorImage.path), FileType.AUTHOR)
            author.imageFile = authorImage
            author.thumbFile = authorImageThumb
        }
        return authorRepository.save(author).run { authorMapper.toDto(this) }
    }

    @Transactional
    fun findById(id: String): AuthorDetailsDto {
        logger.info("Finding author by id: $id")

        return authorRepository.findById(id)
            .map {authorMapper.toDetailsDto(it) }
            .orElseThrow { NotFoundException("Author not found with id: $id") }
    }

    @Transactional
    fun getAuthorDetails(id: String): AuthorDetailsDto {
        logger.info("Getting author details $id")

        return findById(id).apply {
            books = bookRepository.findByAuthorId(id)
                .map { bookMapper.toDto(it) }
                .toList()
        }
    }

    @Transactional
    fun findAll(search: String?,  pageable: Pageable): Page<AuthorDto> {
        logger.info("Finding all authors")

        return authorRepository.findAll(search, pageable)
            .map { authorMapper.toDto(it) }
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
