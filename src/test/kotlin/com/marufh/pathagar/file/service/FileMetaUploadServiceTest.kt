package com.marufh.pathagar.file.service

import com.marufh.pathagar.BaseTest
import com.marufh.pathagar.file.dto.FileDto
import com.marufh.pathagar.file.entity.FileType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockMultipartFile
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Path

class FileMetaUploadServiceTest: BaseTest() {

    @BeforeEach
    fun setup() {
        fileMetaRepository.deleteAll()
    }

    @Test
    fun `test create file`() {
        logger.info("Testing upload file")

        Files.deleteIfExists(Path.of(fileProperties.book).resolve("test-book/test-book.pdf"))

        val mockFile = MockMultipartFile("file",
            "test-book.pdf",
            "application/pdf",
            FileInputStream("src/test/resources/test-book.pdf")
        )
        val fileDto = FileDto(
            name = "test-book",
            file = mockFile,
            fileType = FileType.BOOK
        )

        // When
        val fileMeta = fileUploadService.createFile(fileDto);

        // Then
        assertNotNull(fileMeta.id)
        assertEquals("test-book", fileMeta.name)
        assertEquals("book/test-book/test-book.pdf", fileMeta.path)
        assertEquals(FileType.BOOK, fileMeta.fileType)
        assertTrue(Files.exists(Path.of(fileProperties.book).resolve("test-book/test-book.pdf")))
    }

   @Test
    fun `test update file`() {
        logger.info("Testing update file")

        Files.deleteIfExists(Path.of(fileProperties.book).resolve("tes-book/test-book.jpg"))
        val mockFile = MockMultipartFile("file",
            "test-book.jpg",
            "application/jpeg",
            FileInputStream("src/test/resources/test-book.jpg")
        )
        val fileDto = FileDto( name = "file",
            file =  mockFile,
            fileType = FileType.BOOK
        )

        // When
        val fileMeta = fileUploadService.createFile(fileDto);

        // Then
        assertNotNull(fileMeta.id)
        assertEquals("book/test-book/test-book.jpg", fileMeta.path)
        assertTrue(Files.exists(Path.of(fileProperties.book).resolve("test-book/test-book.jpg")))


       // Update file with new file
       val mockFile2 = MockMultipartFile(
           "file",
           "test-book2.jpg",
           "application/jpeg",
           FileInputStream("src/test/resources/test-book2.jpg")
       )
       val fileDto2 = FileDto(
           id = fileMeta.id,
           name = "file",
           file =  mockFile2,
           fileType = FileType.BOOK
       )

       // When
       val updateFile = fileUploadService.updateFile(fileDto2);

       // Then
       assertEquals(fileMeta.id, updateFile.id)
       assertEquals("book/test-book2/test-book2.jpg", updateFile.path)
       assertTrue(Files.exists(Path.of(fileProperties.book).resolve("test-book2/test-book2.jpg")))
   }

}
