package com.marufh.pathagar.author

import com.marufh.pathagar.BaseTest
import com.marufh.pathagar.author.dto.AuthorCreateRequest
import com.marufh.pathagar.author.entity.Author
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
        println("Cleaning up")
        bookRepository.deleteAll()
        authorRepository.deleteAll()
        categoryRepository.deleteAll()
        fileMetaRepository.deleteAll()
        Files.deleteIfExists(Path.of(fileProperties.author).resolve("test-author/test-author.jpg"))
    }

    @Test
    fun `test create author`() {
        // Given
        val authorCreateRequest = getAuthorDto()

        // When
        val result  = authorService.create(authorCreateRequest)

        // Then
        assert(result.id != null)
        assert(result.name == authorCreateRequest.name)
        assert(result.description == authorCreateRequest.description)
        assert(result.imageFile?.path == "author/test-author/test-author.jpg")
        assert(result.thumbFile?.path == "author/test-author/test-author_thumb.jpg")
    }

    @Test
    fun `test create author exception`() {

        // Given
        val authorDto = getAuthorDto();
        authorDto.file = null

        // When
        val exception = assertThrows<IllegalArgumentException> {
            authorService.create(authorDto)
        }

        // Then
        assert(exception.message == "File is required")
    }

    @Test
    fun `test update  author`() {
        // Given
        val authorCreateRequest = getAuthorDto()


        val authorResponse = authorService.create(authorCreateRequest)

        // When
        val updateName = "Updated Author Name" + UUID.randomUUID().toString()
        val updateDescription = "Updated Author Description"
        val result = authorService.update(AuthorCreateRequest(
                id = authorResponse.id,
                name = updateName,
                description = updateDescription,
                file = null
            )
        )

        // Then
        assert(result.id != null)
        assert(result.name == updateName)
        assert(result.description == updateDescription)
        assert(result.imageFile?.path == "author/test-author/test-author.jpg")
        assert(result.thumbFile?.path == "author/test-author/test-author_thumb.jpg")
    }

    @Test
    fun `test update author not found exception`() {
        // Given
        val authorCreateRequest = getAuthorDto();
        authorCreateRequest.id = "invalid-id"

        // When
        val exception = assertThrows<NotFoundException> {
            authorService.update(authorCreateRequest)
        }

        // Then
        assert(exception.message == "Author not found with id: invalid-id")
    }

    @Test
    fun `test get author by id`() {
        logger.info("Testing get author by id")

        // Given
        val authorDto = authorService.create(getAuthorDto())

        // When
        val authorDetailsDto = authorService.findById(authorDto.id!!)

        // Then
        assert(authorDetailsDto.id != null)
        assert(authorDetailsDto.name == authorDto.name)
        assert(authorDetailsDto.description == authorDto.description)
    }

    @Test
    fun `test get author by id not found exception`() {
        // Given and When
        val exception = assertThrows<NotFoundException> {
            authorService.findById("invalid-id")
        }

        // Then
        assert(exception.message == "Author not found with id: invalid-id")
    }

    @Test
    fun `test get all authors`() {
        // Given
        listOf(
            Author(name = "test author1", description = "test description1"),
            Author(name = "test author2", description = "test description2"),
            Author(name = "test author3", description = "test description3"),
            Author(name = "test author4", description = "test description4"),
            Author(name = "test author5", description = "test description5"),
        ).map { authorRepository.save(it) }

        // When
        val authorDtoPage = authorService.findAll("", PageRequest.of(0, 10))

        // Then
        assert(authorDtoPage.totalElements == 5L)
        assert(authorDtoPage.totalPages == 1)
        assert(authorDtoPage.content.size == 5)
    }


    @Test
    fun `test get all authors details`() {
        // Given
        val book = bookService.create(getBookDto())
        val author = authorService.create(getAuthorDto())

        bookService.updateAuthor(book.id!!, author.id!!, "add")
        val result1 = authorService.getAuthorDetails(author.id!!)
        assert(result1.books?.size == 1)


        bookService.updateAuthor(book.id!!, author.id!!, "remove")
        val result2 = authorService.getAuthorDetails(author.id!!)
        assert(result2.books?.size == 0)
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
        //authorService.findById(authorDto.id!!)
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
        // Given and When
        val exception = assertThrows<NotFoundException> {
            authorService.delete("invalid-id")
        }

        // Then
        assert(exception.message == "Author not found with id: invalid-id")
    }
}
