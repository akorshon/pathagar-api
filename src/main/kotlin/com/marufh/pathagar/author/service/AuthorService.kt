package com.marufh.pathagar.author.service

import com.marufh.pathagar.author.dto.AuthorDto
import com.marufh.pathagar.author.dto.AuthorMapper
import com.marufh.pathagar.author.entity.Author
import com.marufh.pathagar.author.repository.AuthorRepository
import com.marufh.pathagar.book.dto.BookDto
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
class AuthorService(
    private val bookRepository: BookRepository,
    private val fileProperties: FileProperties,
    private val authorMapper: AuthorMapper,
    private val bookMapper: BookMapper,
    private val authorRepository: AuthorRepository) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun create(authorDto: AuthorDto): AuthorDto {
        logger.info("Creating author: ${authorDto.name}")

        authorMapper.toEntity(authorDto).run {
            return authorMapper.toDto(authorRepository.save(this))
        }
    }

    @Transactional
    fun update(authorDto: AuthorDto): AuthorDto {
        logger.info("Updating author: ${authorDto.name}")

        val author = authorRepository.findById(authorDto.id!!)
            .orElseThrow { EntityNotFoundException("Author not found with id: ${authorDto.id}") }
        author.name = authorDto.name
        author.description = authorDto.description
        return authorRepository.save(author).run { authorMapper.toDto(this) }
    }

    fun findById(id: String): AuthorDto {
        logger.info("Finding author by id: $id")

        return authorRepository.findById(id)
            .map {authorMapper.toDto(it) }
            .orElseThrow { NotFoundException("Author not found with id: $id") }
    }

    fun getAuthorDetails(id: String): AuthorDto {
        logger.info("Getting author details $id")

        return findById(id).apply {
            books = bookRepository.findByAuthorId(id)
                .map { bookMapper.toDto(it) }
                .toList()
        }
    }

    fun findAll(search: String?,  pageable: Pageable): Page<AuthorDto> {
        logger.info("Finding all authors")

        return authorRepository.findAll(search, pageable)
            .map { authorMapper.toDto(it) }
    }

    fun delete(id: String) {
        logger.info("Deleting author by id: $id")

        val author =  authorRepository.findById(id)
            .orElseThrow{ NotFoundException("Author not found with id: $id") }

        try {
            Files.delete(Path.of(fileProperties.base +"/"+ author.image))
            Files.delete(Path.of(fileProperties.base +"/"+ author.thumbnail))
            Files.delete(Path.of(fileProperties.base +"/"+ author.image).parent)
        } catch (e: Exception) {
            println("Error deleting file: ${e.message}")
        } finally {
            authorRepository.delete(author);
        }
    }

}
