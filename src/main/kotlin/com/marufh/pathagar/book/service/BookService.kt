package com.marufh.pathagar.book.service

import com.marufh.pathagar.author.repository.AuthorRepository
import com.marufh.pathagar.book.dto.*
import com.marufh.pathagar.book.entity.Book
import com.marufh.pathagar.book.repository.BookRepository
import com.marufh.pathagar.category.model.CategoryRepository
import com.marufh.pathagar.config.FileProperties
import com.marufh.pathagar.exception.NotFoundException
import com.marufh.pathagar.extension.toBookDetailsResponse
import com.marufh.pathagar.extension.toBookResponse
import com.marufh.pathagar.file.dto.FileDto
import com.marufh.pathagar.file.entity.FileType
import com.marufh.pathagar.file.service.FileService
import com.marufh.pathagar.file.service.FileUploadService
import com.marufh.pathagar.file.service.PdfService
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.nio.file.Files
import java.nio.file.Path

@Service
class BookService(
    private val fileService: FileService,
    private val pdfService: PdfService,
    private val bookRepository: BookRepository,
    private val fileProperties: FileProperties,
    private val fileUploadService: FileUploadService,
    private val categoryRepository: CategoryRepository,
    private val authorRepository: AuthorRepository) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun create(bookCreateRequest: BookCreateRequest): BookResponse {
        logger.info("Creating book: ${bookCreateRequest.name}")

        requireNotNull(bookCreateRequest.file) {
            throw IllegalArgumentException("File is required")
        }

        val pdfFile = fileUploadService.createFile(FileDto(
            name = bookCreateRequest.name,
            file = bookCreateRequest.file!!,
            fileType = FileType.BOOK
        ))
        val coverImageFile = pdfService.createCoverImage(Path.of(fileProperties.base, pdfFile.path), FileType.BOOK_THUMB)
        val totalPage = pdfService.getTotalPage(Path.of(fileProperties.base, pdfFile.path).toFile())

        return Book(
            name = bookCreateRequest.name,
            description = bookCreateRequest.description,
            pdfFile = pdfFile,
            coverImage = coverImageFile,
            totalPage = totalPage
        ).let {
            bookRepository.save(it).toBookResponse()
        }
    }

    @Transactional
    fun update(bookCreateRequest: BookCreateRequest): BookResponse {
        logger.info("Updating book: ${bookCreateRequest.name}")

        val book = bookRepository.findById(bookCreateRequest.id!!)
            .orElseThrow { NotFoundException("Book not found with id: ${bookCreateRequest.id}") }

        book.name = bookCreateRequest.name
        book.description = bookCreateRequest.description
        if(bookCreateRequest.file != null) {
            book.pdfFile = fileUploadService.createFile(FileDto(
                name = bookCreateRequest.name,
                file = bookCreateRequest.file!!,
                fileType = FileType.BOOK
            ))
            book.coverImage = pdfService.createCoverImage(Path.of(fileProperties.base +"/"+ book.pdfFile?.path), FileType.BOOK_THUMB)
            book.totalPage = pdfService.getTotalPage(Path.of(fileProperties.base +"/"+ book.pdfFile?.path).toFile())
        }

        return bookRepository.save(book).toBookResponse()
    }

    @Transactional
    fun findById(id: String): BookDetailsResponse {
        logger.info("Finding book: $id")

        return bookRepository.findByIdOrNull(id)?.toBookDetailsResponse()
            ?: throw NotFoundException("Book not found with id: $id")
    }

    @Transactional
    fun updateAuthor(bookId: String, authorId: String, action: String): BookDetailsResponse {
        logger.info("Updating book: $bookId, author: $authorId, action: $action")

        val book = bookRepository.findById(bookId)
            .orElseThrow { NotFoundException("Book not found with id: $bookId") }

        val author = authorRepository.findById(authorId)
            .orElseThrow { NotFoundException("Author not found with id: $authorId") }

        if(action == AuthorAction.ADD.action) {
            logger.info("Adding bookId: $bookId, authorId: $authorId, action: $action")
            book.authors?.add(author)
        } else if(action == AuthorAction.REMOVE.action){
            logger.info("Removing bookId: $bookId, authorId: $authorId, action: $action")
            book.authors?.remove(author)
        }

        return bookRepository.save(book).toBookDetailsResponse()
    }


    @Transactional
    fun updateCategory(bookId: String, categoryId: String, action: String): BookDetailsResponse {
        logger.info("Updating book: $bookId, category: $categoryId, action: $action")

        val book = bookRepository.findById(bookId)
            .orElseThrow { NotFoundException("Book not found with id: $bookId") }

        val category = categoryRepository.findById(categoryId)
            .orElseThrow { NotFoundException("Category not found with id: $categoryId") }

        if(action == AuthorAction.ADD.action) {
            logger.info("Adding bookId: $bookId, categoryId: $categoryId, action: $action")
            book.categories?.add(category)
        } else if(action == AuthorAction.REMOVE.action){
            logger.info("Removing bookId: $bookId, categoryId: $categoryId, action: $action")
            book.categories?.remove(category)
        }

        return bookRepository.save(book).toBookDetailsResponse()
    }


    @Transactional
    fun findAll(search: String?, pageable: Pageable): Page<BookResponse> {
        logger.info("Finding all books")

        return bookRepository.findAll(search, pageable)
            .map { it.toBookResponse() }
    }

    fun delete(id: String) {
        logger.info("Deleting book: $id")

       val book =  bookRepository.findById(id)
            .orElseThrow{ NotFoundException("Book not found with id: $id") }

        if(book.deleted == true) {
            // Hard delete
            fileService.deleteFiles(book.pdfFile?.path!!, book.coverImage?.path!!)
            bookRepository.delete(book)
        } else {
            // Soft delete
            book.deleted = true
            bookRepository.save(book)
        }
    }

}
