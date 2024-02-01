package com.marufh.pathagar.book

import com.marufh.pathagar.BaseTest
import com.marufh.pathagar.book.dto.BookCreateRequest
import com.marufh.pathagar.book.entity.Book
import com.marufh.pathagar.exception.NotFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.nio.file.Files
import java.nio.file.Path
import java.util.*


class BookServiceTest: BaseTest() {

    @BeforeEach
    fun setup() {
        categoryRepository.deleteAll()
        bookRepository.deleteAll()
        authorRepository.deleteAll()
        fileMetaRepository.deleteAll()
        Files.deleteIfExists(Path.of(fileProperties.book).resolve("test-book/test-book.pdf"))
        Files.deleteIfExists(Path.of(fileProperties.book).resolve("test-book/test-book.jpg"))
    }

    @Test
    fun `test create book`() {
        // Given
        val bookDto = getBookDto();

        // When
        val newBook = bookService.create(bookDto)

        // Then
        assert(newBook.id != null)
        assert(newBook.name == bookDto.name)
        assert(newBook.description == bookDto.description)
        assert(newBook.pdfFile?.path == "book/test-book/test-book.pdf")
        assert(newBook.coverImage?.path == "book/test-book/test-book.jpg")
        assert(newBook.totalPage == 2)
    }

    @Test
    fun `test create book exception`() {

        val bookDto = getBookDto();
        bookDto.file = null

        val exception = assertThrows<IllegalArgumentException> {
            bookService.create(bookDto)
        }
        assert(exception.message == "File is required")
    }

    @Test
    fun `test update book`() {
        // Given

        val newBook = bookService.create(getBookDto())
        val updatedName =  "Updated Book Name" + UUID.randomUUID().toString()
        val updatedDescription = "Updated Book Description"

        // When
        val updatedBook = bookService.update(BookCreateRequest(
            id = newBook.id,
            name = updatedName,
            description = updatedDescription,
        ))

        // Then
        assert(updatedBook.id != null)
        assert(updatedBook.name == updatedName)
        assert(updatedBook.description == updatedDescription)
        assert(updatedBook.pdfFile?.path == "book/test-book/test-book.pdf")
        assert(updatedBook.totalPage == 2)
        assert(updatedBook.coverImage?.path == "book/test-book/test-book.jpg")
    }

    @Test
    fun `test update book not found exception`() {

        val bookDto = getBookDto();
        bookDto.id = "id-not-exist"
        val exception = assertThrows<NotFoundException> {
            bookService.update(bookDto)
        }
        assert(exception.message == "Book not found with id: ${bookDto.id}")
    }

    @Test
    fun `test book find by id`() {
        // Given
        val createdBook = bookService.create(getBookDto())
        val bookFound = bookService.findById(createdBook.id!!);

        // Then
        assert(bookFound.id == createdBook.id)
        assert(bookFound.name == createdBook.name)
        assert(bookFound.description == createdBook.description)
        assert(bookFound.pdfFile?.path == "book/test-book/test-book.pdf")
        assert(bookFound.coverImage?.path == "book/test-book/test-book.jpg")
        assert(bookFound.totalPage == 2)
    }

    @Test
    fun `test book find by id not found exception`() {
        val exception = assertThrows<NotFoundException> {
            bookService.findById("id-not-exist")
        }
        assert(exception.message == "Book not found with id: id-not-exist")
    }

    @Test
    fun `test find all`() {
        // Given
        listOf(
            Book(name = "Book 1", description = "Book 1 description1"),
            Book(name = "Book 2", description = "Book 1 description2"),
            Book(name = "Book 3", description = "Book 1 description3"),
            Book(name = "Book 4", description = "Book 1 description4"),
            Book(name = "Book 5", description = "Book 1 description5"),
        ).map { bookRepository.save(it) }

        // When
        val result = bookService.findAll("", PageRequest.of(0, 10))

        // Then
        assert(result is PageImpl)
        assert(result.totalPages == 1)
        assert(result.content.size == 5)
    }

    @Test
    fun `test update author`() {
        // Given
        val book = bookService.create(getBookDto());
        val author = authorService.create(getAuthorDto())

        // Add
        val bookDetailsAdd = bookService.updateAuthor(book.id!!, author.id!!, "add")
        assert(bookDetailsAdd.authors?.size   == 1)

        // Add
        val bookDetailsRemove = bookService.updateAuthor(book.id!!, author.id!!, "remove")
        assert(bookDetailsRemove.authors?.size   == 0)
    }

    @Test
    fun `test update author, book not found exception`() {

        val book = bookService.create(getBookDto());
        val author = authorService.create(getAuthorDto())

        book.id = "id-not-found"
        val exception = assertThrows<NotFoundException> {
            bookService.updateAuthor(book.id!!, author.id!!, "add")
        }
        assert(exception.message == "Book not found with id: ${book.id}")
    }

    @Test
    fun `test update author, author not found exception`() {

        val book = bookService.create(getBookDto());
        val author = authorService.create(getAuthorDto())

        author.id = "id-not-found"
        val exception = assertThrows<NotFoundException> {
            bookService.updateAuthor(book.id!!, author.id!!, "add")
        }
        assert(exception.message == "Author not found with id: ${author.id}")
    }

    @Test
    fun `test update category`() {
        // Given
        var book = bookService.create(getBookDto());
        val category = categoryService.create(getCategoryDto())

        // Add
        val bookDetailsAdd = bookService.updateCategory(book.id!!, category.id!!, "add")
        assert(bookDetailsAdd.categories?.size   == 1)

        // Remove
        val bookDetailsRemove = bookService.updateCategory(book.id!!, category.id!!, "remove")
        assert(bookDetailsRemove.categories?.size   == 0)
    }

    @Test
    fun `test update category, book not found`() {
        val book = bookService.create(getBookDto());
        val category = categoryService.create(getCategoryDto())

        book.id = "id-not-found"
        val exception = assertThrows<NotFoundException> {
            bookService.updateCategory(book.id!!, category.id!!, "add")
        }

        assert(exception.message == "Book not found with id: ${book.id}")
    }

    @Test
    fun `test update category, category not found`() {
        val book = bookService.create(getBookDto());
        val category = categoryService.create(getCategoryDto())

        category.id = "id-not-found"
        val exception = assertThrows<NotFoundException> {
            bookService.updateCategory(book.id!!, category.id!!, "add")
        }
        assert(exception.message == "Category not found with id: ${category.id}")
    }

    @Test
    fun `test soft delete book`() {
        // Given
        val book = bookService.create(getBookDto());

        // When
        bookService.delete(book.id!!)

        assert(Files.exists(Path.of(fileProperties.book).resolve("test-book/test-book.pdf")))
        assert(Files.exists(Path.of(fileProperties.book).resolve("test-book/test-book.jpg")))
        bookRepository.findById(book.id!!).let {
            assert(it.isPresent)
            assert(it.get().deleted == true)
        }
    }

    @Test
    fun `test hard delete book`() {
        // Given
        val book = bookService.create(getBookDto());

        // When
        bookService.delete(book.id!!) // soft delete
        bookService.delete(book.id!!) // hard delete

        assert(!Files.exists(Path.of(fileProperties.book).resolve("test-book/test-book.pdf")))
        assert(!Files.exists(Path.of(fileProperties.book).resolve("test-book/test-book.jpg")))
        assertThrows<NotFoundException> {
            bookService.findById(book.id!!)
        }
    }

    @Test
    fun `test delete book not found exception`() {
        val exception = assertThrows<NotFoundException> {
            bookService.delete("id-not-found")
        }
        assert(exception.message == "Book not found with id: id-not-found")
    }
}
