package com.marufh.pathagar.service

import com.marufh.pathagar.config.FileProperties
import com.marufh.pathagar.dto.BookDto
import com.marufh.pathagar.entity.Book
import com.marufh.pathagar.repository.AuthorRepository
import com.marufh.pathagar.repository.BookRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors

@Service
class BookService(
    private val pdfService: PdfService,
    private val bookRepository: BookRepository,
    private val fileProperties: FileProperties,
    private val authorRepository: AuthorRepository,
    private val fileUploadService: FileUploadService) {

    fun create(bookDto: BookDto): Book {
        val filePath = fileUploadService.upload(bookDto.file!!, Path.of(fileProperties.book), bookDto.file.originalFilename!!);
        val thumbPath = pdfService.convertToThumb(filePath, filePath.parent)

        val book = Book(
            name = bookDto.name,
            deleted = false,
            filePath = getRelativePath(filePath),
            coverImage = getRelativePath(thumbPath)
        )
        return bookRepository.save(book)
    }

    fun update(bookDto: BookDto): Book {
        val book = bookRepository.findById(bookDto.id!!)
            .orElseThrow { EntityNotFoundException("Book not found with id: ${bookDto.id}") }

        book.name = bookDto.name
        book.description = bookDto.description
        return bookRepository.save(book)
    }

    fun updateAuthor(bookId: String, authorId: String, action: String): Book {

        val book = bookRepository.findById(bookId)
            .orElseThrow { EntityNotFoundException("Book not found with id: $bookId") }

        val author = authorRepository.findById(authorId)
            .orElseThrow { EntityNotFoundException("Author not found with id: $authorId") }
        println("${author.name}, ${author.id}")
        if(action == "add") {
            println("bookId: $bookId, authorId: $authorId, action: $action")
            book.authors.add(author)
        } else {
            println("bookId: $bookId, authorId: $authorId, action: $action")
            book.authors.remove(author)
        }

        println(book.authors.stream().map { it.name }?.collect(Collectors.joining(",")))

        return bookRepository.save(book)
    }

    fun findAll(search: String?, pageable: Pageable) = bookRepository.findAll(search, pageable)

    fun delete(id: String) {
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
