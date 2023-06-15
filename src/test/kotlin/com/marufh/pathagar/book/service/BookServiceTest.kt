package com.marufh.pathagar.book.service

import com.marufh.pathagar.BaseTest
import com.marufh.pathagar.book.dto.BookDto
import com.marufh.pathagar.book.entity.Book
import com.marufh.pathagar.file.entity.FileType
import org.junit.jupiter.api.Test
import java.util.*


class BookServiceTest: BaseTest() {

    @Test
    fun `test create book`() {
        logger.info("Testing create book")
        val bookDto = BookDto(
            name = "Test Book",
            description = "Test Book Description",
            filePath = "test.pdf",
            hash = "test-hash",
            size = 100,
            fileType = FileType.BOOK,
            totalPage = 100,
            coverImage = "test-cover-image",
            coverImagePage = 1,
            deleted = false
        )

        val book = bookService.create(bookDto);
        assert(book.id != null)
        assert(book.name == bookDto.name)
        assert(book.description == bookDto.description)
        assert(book.filePath == bookDto.filePath)
        assert(book.hash == bookDto.hash)
        assert(book.size == bookDto.size)
        assert(book.fileType == bookDto.fileType)
        assert(book.totalPage == bookDto.totalPage)
        assert(book.coverImage == bookDto.coverImage)
        assert(book.coverImagePage == bookDto.coverImagePage)
        assert(book.deleted == bookDto.deleted)
    }

    @Test
    fun `test book find by id`() {
        logger.info("Testing update book")

        var book = Book(
            name = "Test Book" + UUID.randomUUID().toString(),
            description = "Test Book Description",
            filePath = "test.pdf",
            hash = "test-hash",
            size = 100,
            fileType = FileType.BOOK,
            totalPage = 100,
            coverImage = "test-cover-image",
            coverImagePage = 1,
            deleted = false
        )
        book = bookRepository.save(book)


        bookService.findById(book.id!!).let {
            assert(it.id == book.id)
            assert(it.name == book.name)
            assert(it.description == book.description)
            assert(it.filePath == book.filePath)
            assert(it.hash == book.hash)
            assert(it.size == book.size)
            assert(it.fileType == book.fileType)
            assert(it.totalPage == book.totalPage)
            assert(it.coverImage == book.coverImage)
            assert(it.coverImagePage == book.coverImagePage)
            assert(it.deleted == book.deleted)
        }
    }

    @Test
    fun `test update author`() {
        logger.info("Testing update author")
    }

    @Test
    fun `test delete book`() {
        logger.info("Testing delete book")
    }
}
