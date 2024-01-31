package com.marufh.pathagar.category.model

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CategoryRepository: JpaRepository<Category, String> {

    @Query("SELECT c FROM Category c WHERE ( ?1 is null or c.name LIKE %?1% ) ")
    fun findAll(search: String?, pageable: Pageable): Page<Category>
}

