package com.marufh.pathagar.service

import com.marufh.pathagar.config.FileProperties
import com.marufh.pathagar.controller.exception.NotFoundException
import com.marufh.pathagar.dto.AuthorDto
import com.marufh.pathagar.entity.Author
import com.marufh.pathagar.repository.AuthorRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Path
import javax.imageio.ImageIO

@Service
class AuthorService(
    private val fileProperties: FileProperties,
    private val fileUploadService: FileUploadService,
    private val authorRepository: AuthorRepository) {

    fun create(authorDto: AuthorDto): Author {
        val filePath = fileUploadService.upload(authorDto.file!!, Path.of(fileProperties.author), authorDto.file.originalFilename!!);
        val thumbPath = fileUploadService.resizeImage(ImageIO.read(filePath.toFile()), filePath.parent.resolve( "${authorDto.name}_thumb.jpg").toFile(), 200, 300)
        val author = Author(
            name = authorDto.name,
            description = authorDto.description,
            deleted = false,
            image = getRelativePath(filePath),
            thumbnail = getRelativePath(thumbPath)
        )
        return authorRepository.save(author);
    }

    fun update(authorDto: AuthorDto): Author {
        val author = findById(authorDto.id!!)
        author.name = authorDto.name
        author.description = authorDto.description
        return authorRepository.save(author);
    }

    fun findById(id: String): Author {
        return authorRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Author not found with id: $id") }
    }

    fun findAll(search: String?,  pageable: Pageable): Page<Author> {
        return authorRepository.findAll(search, pageable)
    }

    fun delete(id: String) {
        val author =  authorRepository.findById(id)
            .orElseThrow{ NotFoundException("Author not found with id: $id") }

        try {
            Files.delete(Path.of(fileProperties.base +"/"+ author.image))
            Files.delete(Path.of(fileProperties.base +"/"+ author.thumbnail))
            Files.delete(Path.of(fileProperties.base +"/"+ author.image).parent)
        } catch (e: Exception) {
            println("Error deleting file: ${e.message}")
        } finally {
            authorRepository.delete(author);
        }
    }

    private fun getRelativePath(filePath: Path): String {
        return Path.of(fileProperties.base).relativize(filePath).toString()
    }
}
