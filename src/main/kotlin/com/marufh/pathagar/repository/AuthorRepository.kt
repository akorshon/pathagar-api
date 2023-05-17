package com.marufh.pathagar.repository

import com.marufh.pathagar.entity.Author
import org.springframework.data.jpa.repository.JpaRepository

interface AuthorRepository: JpaRepository<Author, String> {
}
