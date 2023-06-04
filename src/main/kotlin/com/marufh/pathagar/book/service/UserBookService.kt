package com.marufh.pathagar.book.service

import com.marufh.pathagar.auth.entity.User
import com.marufh.pathagar.book.dto.UserBookDto
import com.marufh.pathagar.book.entity.UserBook
import com.marufh.pathagar.book.entity.UserBookStatus
import com.marufh.pathagar.book.repository.UserBookRepository
import com.marufh.pathagar.exception.NotFoundException
import jakarta.persistence.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class UserBookService(private val userBookRepository: UserBookRepository) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun create(user: User, usrBookDto: UserBookDto): UserBook {
        logger.info("Creating user {} book{}", user.email, usrBookDto.book.name)

        val userBook = UserBook(
            userEmail = user.email,
            book = usrBookDto.book,
            page = usrBookDto.page,
            rating = usrBookDto.rating,
            status = UserBookStatus.READING,
            started = Instant.now()
        );
        return userBookRepository.save(userBook)
    }

    fun update(user: User, usrBookDto: UserBookDto): UserBook {
        logger.info("Updating user: {}, book: {}", user.id, usrBookDto.id)

        val found = userBookRepository.findByUserAndBookId(user.email, usrBookDto.book.id!!)?:
            throw EntityNotFoundException("UserBook not found with user: ${user.email} and book id: ${usrBookDto.book.id}")

        found.page = usrBookDto.page
        found.rating = usrBookDto.rating
        found.status = usrBookDto.status
        found.review = usrBookDto.review
        return userBookRepository.save(found)
    }

    fun findByUserAndBookId(user: User, bookId: String): UserBook {
        logger.info("Finding user book by user: ${user.email}   and book id: $bookId")

        return userBookRepository.findByUserAndBookId(user.email, bookId)
            ?: throw NotFoundException("UserBook not found with user: ${user.email} and book id: $bookId")
    }


    fun findAll(user: User, pageable: Pageable): Page<UserBook> {
        logger.info("Finding all books")

        return userBookRepository.findAll(user.email, pageable)
    }

    fun delete(user: User, userBookId: String) {
        userBookRepository.findByUserAndBookId(user.email, userBookId)
            ?.let { userBookRepository.delete(it) }
            ?: throw EntityNotFoundException("UserBook not found with user: ${user.id} and book id: $userBookId")
    }
}
