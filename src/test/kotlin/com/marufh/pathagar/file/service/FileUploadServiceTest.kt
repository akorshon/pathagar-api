package com.marufh.pathagar.file.service

import com.marufh.pathagar.BaseTest
import com.marufh.pathagar.file.dto.FileDto
import com.marufh.pathagar.file.entity.FileType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockMultipartFile
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Path

class FileUploadServiceTest: BaseTest() {

    @Test
    fun `test create book file`() {
        logger.info("Testing upload book file")

        // Given
        bookRepository.deleteAll()
        Files.deleteIfExists(Path.of(fileProperties.book).resolve("test-book/test-book.pdf"))
        Files.deleteIfExists(Path.of(fileProperties.book).resolve("test-book/test-book.jpg"))
        val mockFile = MockMultipartFile("file",
            "test-book.pdf",
            "application/pdf",
            FileInputStream("src/test/resources/test-book.pdf")
        )
        val fileDto = FileDto(mockFile, FileType.BOOK)

        // When
        val bookDto = fileUploadService.createBookFile(fileDto);

        // Then
        assertNotNull(bookDto.id)
        assertEquals("test-book", bookDto.name)
        assertEquals("book/test-book/test-book.jpg", bookDto.coverImage)
        assertEquals("book/test-book/test-book.pdf", bookDto.filePath)
        assertEquals(FileType.BOOK, bookDto.fileType)
        assertEquals(1, bookDto.totalPage)
        assertEquals(0, bookDto.coverImagePage)

        assertTrue(Files.exists(Path.of(fileProperties.book).resolve("test-book/test-book.pdf")))
        assertTrue(Files.exists(Path.of(fileProperties.book).resolve("test-book/test-book.jpg")))
    }

    @Test
    fun `test create author file`() {
        logger.info("Testing update book file")

        // Given
        bookRepository.deleteAll()
        authorRepository.deleteAll()

        Files.deleteIfExists(Path.of(fileProperties.author).resolve("tes-author/test-author.jpg"))
        val mockFile = MockMultipartFile("file",
            "test-author.jpg",
            "application/jpeg",
            FileInputStream("src/test/resources/test-author.jpg")
        )
        val fileDto = FileDto(mockFile, FileType.AUTHOR_IMAGE)

        // When
        val authorDto = fileUploadService.createAuthorFile(fileDto);

        // Then
        assertNotNull(authorDto.id)
        assertEquals("author/test-author/test-author.jpg" ,authorDto.imagePath)
        assertTrue(Files.exists(Path.of(fileProperties.author).resolve("test-author/test-author.jpg")))
        assertTrue(Files.exists(Path.of(fileProperties.author).resolve("test-author/test-author_thumb.jpg")))

    }

    @Test
    fun `test update book file`() {
        logger.info("Testing upload author file")

    }

    @Test
    fun `test update author file`() {
        logger.info("Testing update author file")

    }

    @Test
    fun `test create book file with invalid file`() {
        logger.info("Testing upload book file with invalid file")

    }


    @Test
    fun `test create author file with invalid file`() {
        logger.info("Testing upload author file with invalid file")

    }

    @Test
    fun `test create book file with invalid file type`() {
        logger.info("Testing upload book file with invalid file type")

    }

    @Test
    fun `test create author file with invalid file type`() {
        logger.info("Testing upload author file with invalid file type")

    }

    @Test
    fun `test update thumb `() {
        logger.info("Testing update thumb")

    }

}
