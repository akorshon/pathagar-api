package com.marufh.pathagar.author.repository

import com.marufh.pathagar.author.entity.Author
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface AuthorRepository: JpaRepository<Author, String> {

    @Query("SELECT a FROM Author a WHERE ( ?1 is null or a.name LIKE %?1% ) ")
    fun findAll(search: String?, pageable: Pageable): Page<Author>

    //@Query("SELECT a FROM Author a JOIN a.books b WHERE b.id = ?1")
    //fun findAuthorByBook(id: String): Set<Author>
}

