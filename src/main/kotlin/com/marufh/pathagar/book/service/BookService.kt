package com.marufh.pathagar.book.service

import com.marufh.pathagar.author.repository.AuthorRepository
import com.marufh.pathagar.book.dto.BookDto
import com.marufh.pathagar.book.dto.BookMapper
import com.marufh.pathagar.book.repository.BookRepository
import com.marufh.pathagar.category.model.CategoryRepository
import com.marufh.pathagar.config.FileProperties
import com.marufh.pathagar.exception.NotFoundException
import com.marufh.pathagar.file.dto.FileDto
import com.marufh.pathagar.file.entity.FileType
import com.marufh.pathagar.file.service.FileUploadService
import com.marufh.pathagar.file.service.PdfService
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.nio.file.Files
import java.nio.file.Path

@Service
class BookService(
    private val pdfService: PdfService,
    private val bookMapper: BookMapper,
    private val bookRepository: BookRepository,
    private val fileProperties: FileProperties,
    private val fileUploadService: FileUploadService,
    private val categoryRepository: CategoryRepository,
    private val authorRepository: AuthorRepository) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun create(bookDto: BookDto): BookDto {
        logger.info("Creating book: ${bookDto.name}")

        if(bookDto.file == null) {
            throw IllegalArgumentException("File is required")
        }

        val pdfFile = fileUploadService.createFile(FileDto(
            name = bookDto.name,
            file = bookDto.file!!,
            fileType = FileType.BOOK
        ))
        val coverImageFile = pdfService.createCoverImage(Path.of(fileProperties.base, pdfFile.path), FileType.BOOK_THUMB)
        val totalPage = pdfService.getTotalPage(Path.of(fileProperties.base, pdfFile.path).toFile())

        return bookMapper.toEntity(bookDto).let {
            it.pdfFile = pdfFile
            it.coverImage = coverImageFile
            it.totalPage = totalPage
            bookMapper.toDto(bookRepository.save(it))
        }
    }

    @Transactional
    fun update(bookDto: BookDto): BookDto {
        logger.info("Updating book: ${bookDto.name}")

        val book = bookRepository.findById(bookDto.id!!)
            .orElseThrow { NotFoundException("Book not found with id: ${bookDto.id}") }

        book.name = bookDto.name
        book.description = bookDto.description
        if(bookDto.file != null) {
            book.pdfFile = fileUploadService.createFile(FileDto(
                name = bookDto.name,
                file = bookDto.file!!,
                fileType = FileType.BOOK
            ))
            book.coverImage = pdfService.createCoverImage(Path.of(fileProperties.base +"/"+ book.pdfFile?.path), FileType.BOOK_THUMB)
            book.totalPage = pdfService.getTotalPage(Path.of(fileProperties.base +"/"+ book.pdfFile?.path).toFile())
        }
        return bookRepository.save(book).run { bookMapper.toDto(this) }
    }

    @Transactional
    fun findById(id: String): BookDto {
        logger.info("Finding book: $id")

        return bookRepository.findById(id)
            .map { bookMapper.toDto(it) }
            .orElseThrow { NotFoundException("Book not found with id: $id") }
    }

    @Transactional
    fun updateAuthor(bookId: String, authorId: String, action: String): BookDto {
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

        return bookRepository.save(book).let { bookMapper.toDto(it) }
    }


    @Transactional
    fun updateCategory(bookId: String, categoryId: String, action: String): BookDto {
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

        return bookRepository.save(book).let { bookMapper.toDto(it) }
    }


    @Transactional
    fun findAll(search: String?, pageable: Pageable): Page<BookDto> {
        logger.info("Finding all books")

        return bookRepository.findAll(search, pageable)
            .map { bookMapper.toDto(it) }
    }

    fun delete(id: String) {
        logger.info("Deleting book: $id")

       val book =  bookRepository.findById(id)
            .orElseThrow{ NotFoundException("Book not found with id: $id") }

        if(book.deleted == true) {
            // Hard delete
            Files.delete(Path.of(fileProperties.base, book.pdfFile?.path))
            Files.delete(Path.of(fileProperties.base, book.coverImage?.path))
            Files.delete(Path.of(fileProperties.base, book.pdfFile?.path).parent)
            bookRepository.delete(book);
        } else {
            // Soft delete
            book.deleted = true
            bookRepository.save(book)
        }
    }

}
