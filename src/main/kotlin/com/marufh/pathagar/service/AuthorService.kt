package com.marufh.pathagar.service

import com.marufh.pathagar.dto.AuthorDto
import com.marufh.pathagar.entity.Author
import com.marufh.pathagar.repository.AuthorRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service

@Service
class AuthorService(
    private val authorRepository: AuthorRepository) {

    fun create(authorDto: AuthorDto): Author {
        val author = Author(authorDto.name, authorDto.description)
        return authorRepository.save(author);
    }

    fun update(authorDto: AuthorDto): Author {
        val author = findById(authorDto.id)
        author.name = authorDto.name
        author.description = authorDto.description
        return authorRepository.save(author);
    }

    fun findById(id: String): Author {
        return authorRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Author not found with id: $id") }
    }

    fun findAll(): List<Author> {
        return authorRepository.findAll()
    }

    fun delete(id: String) {
        val author = findById(id)
        author.deleted = true;
        authorRepository.save(author)
    }
}
