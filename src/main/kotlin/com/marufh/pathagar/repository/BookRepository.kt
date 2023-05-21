package com.marufh.pathagar.repository

import com.marufh.pathagar.entity.Book
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface BookRepository: JpaRepository<Book, String> {

    @Query("SELECT b FROM Book b WHERE ( ?1 is null or b.name LIKE %?1% ) ")
    fun findAll(search: String?, pageable: Pageable): Page<Book>
}
