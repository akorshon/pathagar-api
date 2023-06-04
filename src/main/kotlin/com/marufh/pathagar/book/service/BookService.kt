package com.marufh.pathagar.book.service

import com.marufh.pathagar.config.FileProperties
import com.marufh.pathagar.book.dto.BookDto
import com.marufh.pathagar.book.entity.Book
import com.marufh.pathagar.author.repository.AuthorRepository
import com.marufh.pathagar.book.repository.BookRepository
import com.marufh.pathagar.exception.AlreadyExistException
import com.marufh.pathagar.file.service.PdfService
import jakarta.persistence.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Path

@Service
class BookService(
    private val bookRepository: BookRepository,
    private val fileProperties: FileProperties,
    private val authorRepository: AuthorRepository) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun create(bookDto: BookDto): Book {
        logger.info("Creating book: ${bookDto.name}")

        val book = Book(
            name = bookDto.name,
            description = bookDto.description,
            filePath = bookDto.filePath,
            hash = bookDto.hash,
            size = bookDto.size,
            fileType = bookDto.fileType,
            totalPage = bookDto.totalPage,
            coverImage = bookDto.coverImage,
            coverImagePage = bookDto.coverImagePage,
            deleted = bookDto.deleted,

        )
        return bookRepository.save(book)
    }

    fun update(bookDto: BookDto): Book {
        logger.info("Updating book: ${bookDto.name}")

        val book = bookRepository.findById(bookDto.id!!)
            .orElseThrow { EntityNotFoundException("Book not found with id: ${bookDto.id}") }

        book.name = bookDto.name
        book.description = bookDto.description
        return bookRepository.save(book)
    }

    fun updateAuthor(bookId: String, authorId: String, action: String): Book {
        logger.info("Updating book: $bookId, author: $authorId, action: $action")

        val book = bookRepository.findById(bookId)
            .orElseThrow { EntityNotFoundException("Book not found with id: $bookId") }

        val author = authorRepository.findById(authorId)
            .orElseThrow { EntityNotFoundException("Author not found with id: $authorId") }

        if(action == "add") {
            println("bookId: $bookId, authorId: $authorId, action: $action")
            book.authors.add(author)
        } else {
            println("bookId: $bookId, authorId: $authorId, action: $action")
            book.authors.remove(author)
        }

        return bookRepository.save(book)
    }

    fun findAll(search: String?, pageable: Pageable): Page<Book> {
        logger.info("Finding all books")

        return bookRepository.findAll(search, pageable)
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


    private fun getRelativePath(filePath: Path): String {
        return Path.of(fileProperties.base).relativize(filePath).toString()
    }
}
