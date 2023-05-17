package com.marufh.pathagar.repository

import com.marufh.pathagar.entity.Book
import org.springframework.data.jpa.repository.JpaRepository

interface BookRepository: JpaRepository<Book, String> {
}
