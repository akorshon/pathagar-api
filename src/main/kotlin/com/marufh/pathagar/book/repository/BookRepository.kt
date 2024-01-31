package com.marufh.pathagar.book.repository

import com.marufh.pathagar.book.entity.Book
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface BookRepository: JpaRepository<Book, String> {

    @Query("SELECT b FROM Book b WHERE ( ?1 is null or b.name LIKE %?1% ) ")
    fun findAll(search: String?, pageable: Pageable): Page<Book>

    //@Query("SELECT b FROM Book b WHERE b.pdfFile.hash = ?1 ")
    //fun findByHash(search: String): Book?

    @Query("SELECT b FROM Book b left join b.authors a WHERE a.id = ?1 ")
    fun findByAuthorId(authorId: String): Set<Book>

    @Query("SELECT b FROM Book b left join b.categories c WHERE c.id = ?1 ")
    fun findByCategoryId(categoryId: String): Set<Book>
}
