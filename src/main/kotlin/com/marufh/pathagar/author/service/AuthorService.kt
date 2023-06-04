package com.marufh.pathagar.author.service

import com.marufh.pathagar.author.dto.AuthorDto
import com.marufh.pathagar.author.entity.Author
import com.marufh.pathagar.author.repository.AuthorRepository
import com.marufh.pathagar.book.repository.BookRepository
import com.marufh.pathagar.config.FileProperties
import com.marufh.pathagar.exception.NotFoundException
import jakarta.persistence.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Path

@Service
class AuthorService(
    private val bookRepository: BookRepository,
    private val fileProperties: FileProperties,
    private val authorRepository: AuthorRepository) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun create(authorDto: AuthorDto): Author {
        logger.info("Creating author: ${authorDto.name}")

        val author = Author(
            name = authorDto.name,
            description = authorDto.description,
            deleted = false,
            image = authorDto.image,
            thumbnail = authorDto.thumbnail
        )
        return authorRepository.save(author);
    }

    fun update(authorDto: AuthorDto): Author {
        logger.info("Updating author: ${authorDto.name}")

        val author = findById(authorDto.id!!)
        author.name = authorDto.name
        author.description = authorDto.description
        return authorRepository.save(author);
    }

    fun findById(id: String): Author {
        logger.info("Finding author by id: $id")

        return authorRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Author not found with id: $id") }
    }

    fun getAuthorDetails(id: String): AuthorDto {
        logger.info("Getting author details $id")

        val author = findById(id);
        val books = bookRepository.findByAuthorId(id)

        return AuthorDto(
            id = author.id,
            name = author.name,
            description = author.description,
            image = author.image,
            thumbnail = author.thumbnail,
            books = books
        )

    }

    fun findAll(search: String?,  pageable: Pageable): Page<Author> {
        logger.info("Finding all authors")

        return authorRepository.findAll(search, pageable)
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

    private fun getRelativePath(filePath: Path): String {
        return Path.of(fileProperties.base).relativize(filePath).toString()
    }
}
