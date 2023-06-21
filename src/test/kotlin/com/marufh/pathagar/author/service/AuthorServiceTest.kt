package com.marufh.pathagar.author.service

import com.marufh.pathagar.BaseTest
import com.marufh.pathagar.exception.NotFoundException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.domain.PageRequest
import java.util.*

class AuthorServiceTest: BaseTest() {

    @Test
    fun `test create author`() {
        logger.info("Testing create author")

        // Given
        val authorDto = getAuthorDto();

        // When
        authorService.create(authorDto).apply {

            // Then
            assert(id != null)
            assert(name == authorDto.name)
            assert(description == authorDto.description)
            assert(deleted == authorDto.deleted)
        }
    }

    @Test
    fun `test update  author`() {
        logger.info("Testing create author")

        // Given
        val authorDto = authorService.create(getAuthorDto())
        authorDto.name = "Updated Author Name" + UUID.randomUUID().toString()
        authorDto.description = "Updated Author Description"

        // When
        authorService.update(authorDto).apply {

            // Then
            assert(id != null)
            assert(name == authorDto.name)
            assert(description == authorDto.description)
            assert(deleted == authorDto.deleted)
        }
    }

    @Test
    fun `test get author by id`() {
        logger.info("Testing get author by id")

        // Given
        val authorDto = authorService.create(getAuthorDto())

        // When
        authorService.findById(authorDto.id!!).apply {

            // Then
            assert(id != null)
            assert(name == authorDto.name)
            assert(description == authorDto.description)
            assert(deleted == authorDto.deleted)
        }
    }

    @Test
    fun `test get author by id with invalid id`() {
        logger.info("Testing get author by id with invalid id")

        assertThrows<NotFoundException> {
            authorService.findById("invalid-id")
        }
    }

    @Test
    fun `test get all authors`() {
        logger.info("Testing get all authors")

        // Given
        bookRepository.deleteAll()
        authorRepository.deleteAll()
        val authorList = listOf(
            authMapper.toEntity(getAuthorDto()),
            authMapper.toEntity(getAuthorDto()),
            authMapper.toEntity(getAuthorDto()),
            authMapper.toEntity(getAuthorDto()),
            authMapper.toEntity(getAuthorDto()),
        )
        authorRepository.saveAll(authorList)

        // When
        authorService.findAll("", PageRequest.of(0, 10)).apply {

            // Then
            assert(totalElements == 5L)
            assert(totalPages == 1)
            assert(content.size == 5)
        }
    }


    @Test
    fun `test get all authors with search`() {
        logger.info("Testing get all authors with search")

        // Given
        val book1 = bookService.create(getBookDto())
        val book2 = bookService.create(getBookDto())
        val author = authorService.create(getAuthorDto())

        bookService.updateAuthor(book1.id!!, author.id!!, "add")
        bookService.updateAuthor(book2.id!!, author.id!!, "add")

        authorService.getAuthorDetails(author.id!!).apply {
            assert(books?.size == 2)
        }
    }

    @Test
    fun `test delete author`() {
        logger.info("Testing delete author")

        // Given
        val authorDto = authorService.create(getAuthorDto())

        // When
        authorService.delete(authorDto.id!!)

        // Then
        assertThrows<NotFoundException> {
            authorService.findById(authorDto.id!!)
        }
    }
}
