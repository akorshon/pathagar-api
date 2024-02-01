package com.marufh.pathagar.book.service

import com.marufh.pathagar.auth.entity.User
import com.marufh.pathagar.book.dto.UserBookDto
import com.marufh.pathagar.book.dto.UserBookRequest
import com.marufh.pathagar.book.dto.UserBookResponse
import com.marufh.pathagar.book.entity.Book
import com.marufh.pathagar.book.entity.UserBook
import com.marufh.pathagar.book.entity.UserBookStatus
import com.marufh.pathagar.book.repository.BookRepository
import com.marufh.pathagar.book.repository.UserBookRepository
import com.marufh.pathagar.exception.NotFoundException
import com.marufh.pathagar.extension.toUserBookResponse
import jakarta.persistence.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class UserBookService(
    private val bookRepository: BookRepository,
    private val userBookRepository: UserBookRepository) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun create(user: User, book: Book): UserBookResponse {
        logger.info("Creating user {} book{}", user.email, book.name)

        val userBook = UserBook(
            userEmail = user.email,
            book = book,
            page = 0,
            rating = null,
            status = UserBookStatus.READING,
            started = Instant.now()
        )
        return userBookRepository.save(userBook).toUserBookResponse()
    }

    fun update(user: User, usrBookRequest: UserBookRequest): UserBookResponse {
        logger.info("Updating user: {}, book: {}", user.id, usrBookRequest.id)

        val found = userBookRepository.findByUserAndBookId(user.email, usrBookRequest.book.id!!)?:
            throw EntityNotFoundException("UserBook not found with user: ${user.email} and book id: ${usrBookRequest.book.id}")

        found.page = usrBookRequest.page
        found.rating = usrBookRequest.rating
        found.status = usrBookRequest.status
        found.review = usrBookRequest.review
        return userBookRepository.save(found).toUserBookResponse()
    }

    fun findByUserAndBookId(user: User, bookId: String): UserBookResponse {
        logger.info("Finding user book by user: ${user.email}   and book id: $bookId")

        return userBookRepository.findByUserAndBookId(user.email, bookId).let {
            if (it == null) {
                val book = bookRepository.findById(bookId).orElseThrow { NotFoundException("Book not found with id: $bookId") }
                create(user, book)
            } else {
                it.toUserBookResponse()
            }
        }
    }


    fun findAll(user: User, pageable: Pageable): Page<UserBookResponse> {
        logger.info("Finding all books")

        return userBookRepository.findAll(user.email, pageable)
            .map { it.toUserBookResponse() }
    }

    fun findAllByStatus( user: User, status: String, pageable: Pageable ): Page<UserBookResponse> {
        logger.info("Finding all books by status")
        val readingStatus = UserBookStatus.valueOf(status)
        return userBookRepository.findAllByStatus(user.email, readingStatus, pageable)
            .map { it.toUserBookResponse() }
    }

    fun delete(user: User, userBookId: String) {
        userBookRepository.findByUserAndBookId(user.email, userBookId)
            ?.let { userBookRepository.delete(it) }
            ?: throw EntityNotFoundException("UserBook not found with user: ${user.id} and book id: $userBookId")
    }
}
