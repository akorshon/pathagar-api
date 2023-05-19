package com.marufh.pathagar.service

import com.marufh.pathagar.config.FileProperties
import com.marufh.pathagar.dto.BookDto
import com.marufh.pathagar.entity.Book
import com.marufh.pathagar.repository.BookRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Path

@Service
class BookService(
    private val pdfService: PdfService,
    private val fileUploadService: FileUploadService,
    private val fileProperties: FileProperties,
    private val bookRepository: BookRepository) {

    fun create(bookDto: BookDto): Book {
        val filePath = fileUploadService.upload(bookDto.file, Path.of(fileProperties.book), bookDto.file.originalFilename!!);
        val thumbPath = pdfService.convertToThumb(filePath, filePath.parent)

        val book = Book(
            name = bookDto.name,
            deleted = false,
            filePath = getRelativePath(filePath),
            coverImage = getRelativePath(thumbPath)
        )
        return bookRepository.save(book)
    }

    fun findAll(pageable: Pageable) = bookRepository.findAll(pageable)

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
