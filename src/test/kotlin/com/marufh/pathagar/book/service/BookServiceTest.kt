package com.marufh.pathagar.book.service

import com.marufh.pathagar.BaseTest
import com.marufh.pathagar.book.entity.Book
import jakarta.persistence.EntityNotFoundException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.util.*


class BookServiceTest: BaseTest() {

    @Test
    fun `test create book`() {
        logger.info("Testing create book")

        // Given
        val bookDto = getBookDto();

        // When
        bookService.create(bookDto).apply {

            // Then
            assert(id != null)
            assert(name == bookDto.name)
            assert(description == bookDto.description)
            assert(filePath == bookDto.filePath)
            assert(hash == bookDto.hash)
            assert(size == bookDto.size)
            assert(fileType == bookDto.fileType)
            assert(totalPage == bookDto.totalPage)
            assert(coverImage == bookDto.coverImage)
            assert(coverImagePage == bookDto.coverImagePage)
            assert(deleted == bookDto.deleted)
        }
    }

    @Test
    fun `test update  book`() {
        logger.info("Testing create book")

        // Given
        val bookDto = bookService.create(getBookDto())
        bookDto.name = "Updated Book Name" + UUID.randomUUID().toString()
        bookDto.description = "Updated Book Description"

        // When
        bookService.update(bookDto).apply {

            // Then
            assert(id != null)
            assert(name == bookDto.name)
            assert(description == bookDto.description)
            assert(filePath == bookDto.filePath)
            assert(hash == bookDto.hash)
            assert(size == bookDto.size)
            assert(fileType == bookDto.fileType)
            assert(totalPage == bookDto.totalPage)
            assert(coverImage == bookDto.coverImage)
            assert(coverImagePage == bookDto.coverImagePage)
            assert(deleted == bookDto.deleted)
        }
    }

    @Test
    fun `test book find by id`() {
        logger.info("Testing update book")

        // Given
        val book = bookService.create(getBookDto())

        //
        bookService.findById(book.id!!).apply {

            // Then
            assert(id == book.id)
            assert(name == book.name)
            assert(description == book.description)
            assert(filePath == book.filePath)
            assert(hash == book.hash)
            assert(size == book.size)
            assert(fileType == book.fileType)
            assert(totalPage == book.totalPage)
            assert(coverImage == book.coverImage)
            assert(coverImagePage == book.coverImagePage)
            assert(deleted == book.deleted)
        }
    }

    @Test
    fun `test book find by id not found`() {
        assertThrows<EntityNotFoundException> {
            bookService.findById("id-not-exist")
        }
    }

    @Test
    fun `test find all`() {
        logger.info("Testing find all book")

        // Given
        bookRepository.deleteAll();
        val bookList = listOf<Book>(
            bookMapper.toEntity(getBookDto()),
            bookMapper.toEntity(getBookDto()),
            bookMapper.toEntity(getBookDto()),
            bookMapper.toEntity(getBookDto()),
            bookMapper.toEntity(getBookDto()),
        )
        bookRepository.saveAll(bookList)

        // When
        val result = bookService.findAll("", PageRequest.of(0, 10))

        // Then
        assert(result is PageImpl)
        assert(result.totalPages == 1)
        assert(result.content.size == 5)

    }

    @Test
    fun `test update author`() {
        logger.info("Testing update author")

        // Given
        var book = bookService.create(getBookDto());
        val author = authorService.create(getAuthorDto())

        // When
        book = bookService.updateAuthor(book.id!!, author.id!!, "add")

        // Then
        assert(book.authors?.size   == 1)
    }

    @Test
    fun `test delete book`() {
        logger.info("Testing delete book")

        // Given
        val book = bookService.create(getBookDto());

        // When
        bookService.delete(book.id!!)

        // Then
        assertThrows<EntityNotFoundException> {
            bookService.findById(book.id!!)
        }
    }
}
