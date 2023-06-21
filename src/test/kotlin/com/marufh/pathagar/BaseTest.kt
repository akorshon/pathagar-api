package com.marufh.pathagar

import com.fasterxml.jackson.databind.ObjectMapper
import com.marufh.pathagar.author.dto.AuthorDto
import com.marufh.pathagar.author.dto.AuthorMapper
import com.marufh.pathagar.author.repository.AuthorRepository
import com.marufh.pathagar.author.service.AuthorService
import com.marufh.pathagar.book.dto.BookDto
import com.marufh.pathagar.book.dto.BookMapper
import com.marufh.pathagar.book.repository.BookRepository
import com.marufh.pathagar.book.service.BookService
import com.marufh.pathagar.config.FileProperties
import com.marufh.pathagar.file.entity.FileType
import com.marufh.pathagar.file.service.FileDownloadService
import com.marufh.pathagar.file.service.FileUploadService
import com.marufh.pathagar.file.service.ImageResizeService
import com.marufh.pathagar.file.service.PdfService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import java.util.*


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BaseTest {

    val logger = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var bookMapper: BookMapper

    @Autowired
    lateinit var authMapper: AuthorMapper

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var bookService: BookService

    @Autowired
    lateinit var authorService: AuthorService

    @Autowired
    lateinit var fileUploadService: FileUploadService

    @Autowired
    lateinit var fileDownloadService: FileDownloadService

    @Autowired
    lateinit var authorRepository: AuthorRepository

    @Autowired
    lateinit var bookRepository: BookRepository

    @Autowired
    lateinit var imageResizeService: ImageResizeService

    @Autowired
    lateinit var pdfService: PdfService

    @Autowired
    lateinit var fileProperties: FileProperties

    fun getAuthorDto(): AuthorDto {
        return AuthorDto(
            name = "Test Author" + UUID.randomUUID().toString(),
            description = "Test Author Description",
            deleted = false,
        )
    }

    fun getBookDto(): BookDto {
        return BookDto(
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
    }

    fun uploadFile() {

    }
}
