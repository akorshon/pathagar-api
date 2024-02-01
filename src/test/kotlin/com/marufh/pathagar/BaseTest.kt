package com.marufh.pathagar

import com.fasterxml.jackson.databind.ObjectMapper
import com.marufh.pathagar.auth.entity.Role
import com.marufh.pathagar.auth.entity.User
import com.marufh.pathagar.auth.repository.UserRepository
import com.marufh.pathagar.auth.service.HashService
import com.marufh.pathagar.auth.service.TokenService
import com.marufh.pathagar.auth.service.UserService
import com.marufh.pathagar.author.dto.AuthorCreateRequest
import com.marufh.pathagar.author.dto.AuthorDto
import com.marufh.pathagar.author.repository.AuthorRepository
import com.marufh.pathagar.author.service.AuthorService
import com.marufh.pathagar.book.dto.BookCreateRequest
import com.marufh.pathagar.book.dto.BookDto
import com.marufh.pathagar.book.repository.BookRepository
import com.marufh.pathagar.book.service.BookService
import com.marufh.pathagar.category.dto.CategoryCreateRequest
import com.marufh.pathagar.category.dto.CategoryDto
import com.marufh.pathagar.category.model.CategoryRepository
import com.marufh.pathagar.category.service.CategoryService
import com.marufh.pathagar.config.FileProperties
import com.marufh.pathagar.file.repository.FileMetaRepository
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
import java.io.FileInputStream
import java.util.*


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BaseTest {

    val logger = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var bookService: BookService

    @Autowired
    lateinit var authorService: AuthorService

    @Autowired
    lateinit var categoryService: CategoryService

    @Autowired
    lateinit var fileUploadService: FileUploadService

    @Autowired
    lateinit var fileDownloadService: FileDownloadService

    @Autowired
    lateinit var authorRepository: AuthorRepository

    @Autowired
    lateinit var categoryRepository: CategoryRepository

    @Autowired
    lateinit var bookRepository: BookRepository

    @Autowired
    lateinit var imageResizeService: ImageResizeService

    @Autowired
    lateinit var pdfService: PdfService

    @Autowired
    lateinit var fileMetaRepository: FileMetaRepository

    @Autowired
    lateinit var fileProperties: FileProperties

    @Autowired
    lateinit var hashService: HashService

    @Autowired
    lateinit var tokenService: TokenService

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var userRepository: UserRepository

    fun getUser(): User {
        return User(
            email = "test" + UUID.randomUUID().toString() + "@gmail.com",
            password = "password",
            roles = setOf(Role.ROLE_USER)
        )
    }

    fun getAuthorDto(): AuthorCreateRequest = AuthorCreateRequest(
        name = "Test Author" + UUID.randomUUID().toString(),
        description = "Test Author Description",
        file = MockMultipartFile("file",
            "test-author.jpg",
            "image/jpeg",
            FileInputStream("src/test/resources/test-author.jpg")
        )
    )

    fun getCategoryDto(): CategoryCreateRequest = CategoryCreateRequest(
        name = "Test Category" + UUID.randomUUID().toString(),
        file = MockMultipartFile("file",
            "test-category.jpg",
            "image/jpeg",
            FileInputStream("src/test/resources/test-category.jpg")
        ),
        description = "Test  Description",
    )

    fun getBookDto(): BookCreateRequest = BookCreateRequest(
        file = MockMultipartFile("file",
            "test-book.pdf",
            "application/pdf",
            FileInputStream("src/test/resources/test-book.pdf")
        ),
        name = "Test Book" + UUID.randomUUID().toString(),
        description = "Test Book Description",
    )

}
