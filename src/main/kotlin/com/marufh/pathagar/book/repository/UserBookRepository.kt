package com.marufh.pathagar.book.repository

import com.marufh.pathagar.book.entity.UserBook
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserBookRepository: JpaRepository<UserBook, String> {

    @Query("SELECT ub FROM UserBook ub " +
            "left join ub.book b " +
            "WHERE ub.userEmail = ?1 and b.id = ?2")
    fun findByUserAndBookId(userEmail: String, bookId: String): UserBook?

    @Query("SELECT ub FROM UserBook ub " +
            "join  ub.book b " +
            "WHERE ub.userEmail = ?1")
    fun findAll(userEmail: String, pageable: Pageable): Page<UserBook>
}
