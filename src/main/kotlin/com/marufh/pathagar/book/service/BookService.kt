package com.marufh.pathagar.book.service

import com.marufh.pathagar.config.FileProperties
import com.marufh.pathagar.book.dto.BookDto
import com.marufh.pathagar.book.entity.Book
import com.marufh.pathagar.author.repository.AuthorRepository
import com.marufh.pathagar.book.dto.BookMapper
import com.marufh.pathagar.book.repository.BookRepository
import com.marufh.pathagar.exception.AlreadyExistException
import com.marufh.pathagar.file.service.PdfService
import jakarta.persistence.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.nio.file.Files
import java.nio.file.Path

@Service
class BookService(
    private val bookRepository: BookRepository,
    private val fileProperties: FileProperties,
    private val bookMapper: BookMapper,
    private val authorRepository: AuthorRepository) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun create(bookDto: BookDto): BookDto {
        logger.info("Creating book: ${bookDto.name}")
        bookMapper.toEntity(bookDto).run {
            return bookMapper.toDto(bookRepository.save(this))
        }
    }

    @Transactional
    fun update(bookDto: BookDto): BookDto {
        logger.info("Updating book: ${bookDto.name}")

        val book = bookRepository.findById(bookDto.id!!)
            .orElseThrow { EntityNotFoundException("Book not found with id: ${bookDto.id}") }

        book.name = bookDto.name
        book.description = bookDto.description
        return bookRepository.save(book).run { bookMapper.toDto(this) }
    }

    @Transactional
    fun findById(id: String): BookDto {
        logger.info("Finding book: $id")

        return bookRepository.findById(id)
            .map { bookMapper.toDto(it) }
            .orElseThrow { EntityNotFoundException("Book not found with id: $id") }
    }

    @Transactional
    fun updateAuthor(bookId: String, authorId: String, action: String): BookDto {
        logger.info("Updating book: $bookId, author: $authorId, action: $action")

        val book = bookRepository.findById(bookId)
            .orElseThrow { EntityNotFoundException("Book not found with id: $bookId") }

        val author = authorRepository.findById(authorId)
            .orElseThrow { EntityNotFoundException("Author not found with id: $authorId") }

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
    fun findAll(search: String?, pageable: Pageable): Page<BookDto> {
        logger.info("Finding all books")

        return bookRepository.findAll(search, pageable)
            .map { bookMapper.toDto(it) }
    }

    fun delete(id: String) {
        logger.info("Deleting book: $id")

       val book =  bookRepository.findById(id)
            .orElseThrow{ EntityNotFoundException("Book not found with id: $id") }

        try {
            Files.delete(Path.of(fileProperties.base +"/"+ book.filePath))
            Files.delete(Path.of(fileProperties.base +"/"+ book.coverImage))
            Files.delete(Path.of(fileProperties.base +"/"+ book.filePath).parent)
        } catch (e: Exception) {
            println("Error deleting file: ${e.message}")
        } finally {
            bookRepository.delete(book);
        }
    }

}
