package com.marufh.pathagar.file.service

import com.marufh.pathagar.BaseTest
import com.marufh.pathagar.file.dto.FileDto
import com.marufh.pathagar.file.entity.FileType
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockMultipartFile
import java.io.FileInputStream

class FileDownloadServiceTest: BaseTest() {

    @BeforeEach
    fun setup() {
        authorRepository.deleteAllInBatch()
        bookRepository.deleteAllInBatch()
    }
    
    @Test
    fun `test download book file`() {
        logger.info("Testing download book file")

        // Given
        val mockFile = MockMultipartFile("file",
            "test-book.pdf",
            "application/pdf",
            FileInputStream("src/test/resources/test-book.pdf")
        )
        val fileDto = FileDto(mockFile, FileType.BOOK)
        fileUploadService.createBookFile(fileDto)

        // When
        val bookResource = fileDownloadService.getFile("book/test-book/test-book.pdf");
        val bookThumbResource = fileDownloadService.getFile("book/test-book/test-book.jpg");
        assertNotNull(bookResource)
        assertNotNull(bookThumbResource)
    }

    @Test
    fun `test download author file`() {
        logger.info("Testing download author file")

        // Given
        val mockFile = MockMultipartFile("file",
            "test-author.jpg",
            "application/jpeg",
            FileInputStream("src/test/resources/test-author.jpg")
        )
        val fileDto = FileDto(mockFile, FileType.AUTHOR_IMAGE)
        fileUploadService.createAuthorFile(fileDto)

        // When
        val authorResource = fileDownloadService.getFile("author/test-author/test-author.jpg");
        val authorThumbResource = fileDownloadService.getFile("author/test-author/test-author_thumb.jpg");
        assertNotNull(authorResource)
        assertNotNull(authorThumbResource)
    }

}
