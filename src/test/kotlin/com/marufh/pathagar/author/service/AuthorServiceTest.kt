package com.marufh.pathagar.author.service

import com.marufh.pathagar.BaseTest
import com.marufh.pathagar.exception.NotFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.domain.PageRequest
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

class AuthorServiceTest: BaseTest() {

    @BeforeEach
    fun setup() {
        bookRepository.deleteAll()
        authorRepository.deleteAll()
        fileMetaRepository.deleteAll()
        Files.deleteIfExists(Path.of(fileProperties.author).resolve("test-author/test-author.jpg"))
    }

    @Test
    fun `test create author`() {
        // Given
        val authorDto = getAuthorDto();

        // When
        val author = authorService.create(authorDto)

        // Then
        assert(author.id != null)
        assert(author.name == authorDto.name)
        assert(author.description == authorDto.description)
        assert(author.imageFile?.path == "author/test-author/test-author.jpg")
        assert(author.thumbFile?.path == "author/test-author/test-author_thumb.jpg")
        assert(author.deleted == authorDto.deleted)
    }

    @Test
    fun `test create author exception`() {
        val authorDto = getAuthorDto();
        authorDto.file = null

        val exception = assertThrows<IllegalArgumentException> {
            authorService.create(authorDto)
        }
        assert(exception.message == "File is required")
    }

    @Test
    fun `test update  author`() {
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
            assert(imageFile?.path == "author/test-author/test-author.jpg")
            assert(thumbFile?.path == "author/test-author/test-author_thumb.jpg")
        }
    }

    @Test
    fun `test update author not found exception`() {
        val authorDto = getAuthorDto();
        authorDto.id = "invalid-id"
        val exception = assertThrows<NotFoundException> {
            authorService.update(authorDto)
        }
        assert(exception.message == "Author not found with id: invalid-id")
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
    fun `test get author by id not found exception`() {
        val exception = assertThrows<NotFoundException> {
            authorService.findById("invalid-id")
        }
        assert(exception.message == "Author not found with id: invalid-id")
    }

    @Test
    fun `test get all authors`() {
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
    fun `test get all authors details`() {
        // Given
        val book = bookService.create(getBookDto())
        val author = authorService.create(getAuthorDto())

        bookService.updateAuthor(book.id!!, author.id!!, "add")
        authorService.getAuthorDetails(author.id!!).apply {
            assert(books?.size == 1)
        }

        bookService.updateAuthor(book.id!!, author.id!!, "remove")
        authorService.getAuthorDetails(author.id!!).apply {
            assert(books?.size == 0)
        }
    }

    @Test
    fun `test delete author soft delete`() {
        logger.info("Testing delete author")

        // Given
        val authorDto = authorService.create(getAuthorDto())

        // When
        authorService.delete(authorDto.id!!)

        // Then
        assert(Files.exists(Path.of(fileProperties.author).resolve("test-author/test-author.jpg")))
        assert(Files.exists(Path.of(fileProperties.author).resolve("test-author/test-author_thumb.jpg")))
        authorService.findById(authorDto.id!!).let {
            assert(it.deleted == true)
        }
    }

    @Test
    fun `test delete author hard delete`() {
        logger.info("Testing delete author")

        // Given
        val authorDto = authorService.create(getAuthorDto())

        // When
        authorService.delete(authorDto.id!!) // Soft delete
        authorService.delete(authorDto.id!!) // Hard delete

        // Then
        assert(Files.notExists(Path.of(fileProperties.author).resolve("test-author/test-author.jpg")))
        assert(Files.notExists(Path.of(fileProperties.author).resolve("test-author/test-author_thumb.jpg")))
        assertThrows<NotFoundException> {
            authorService.findById(authorDto.id!!)
        }
    }

    @Test
    fun `test delete author not found exception`() {
        val exception = assertThrows<NotFoundException> {
            authorService.delete("invalid-id")
        }
        assert(exception.message == "Author not found with id: invalid-id")
    }
}
