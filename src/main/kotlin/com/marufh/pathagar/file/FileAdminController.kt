package com.marufh.pathagar.file

import com.marufh.pathagar.file.dto.FileDto
import com.marufh.pathagar.file.entity.FileMeta
import com.marufh.pathagar.file.service.FileUploadService
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin/file")
class FileAdminController(
    private val fileUploadService: FileUploadService) {

    fun create(@ModelAttribute fileDto: FileDto): FileMeta {
        return fileUploadService.createFile(fileDto)
    }

    @PutMapping("/book")
    fun update(@ModelAttribute fileDto: FileDto): FileMeta {
        return fileUploadService.updateFile(fileDto)
    }


    /*@PostMapping("/book")
    fun createBook(@ModelAttribute fileDto: FileDto): BookDto {
        return fileUploadService.createBookFile(fileDto)
    }

    @PutMapping("/book")
    fun updateBook(@ModelAttribute fileDto: FileDto): BookDto {
      return fileUploadService.updateBookFile(fileDto)
    }

    @PostMapping("/author")
    fun createAuthor(@ModelAttribute fileDto: FileDto): AuthorDto {
      return fileUploadService.createAuthorFile(fileDto)
    }

    @PutMapping("/author")
    fun updateAuthor(@ModelAttribute fileDto: FileDto): AuthorDto {
      return fileUploadService.updateAuthorFile(fileDto)
    }

    @PostMapping("/category")
    fun createCategory(@ModelAttribute fileDto: FileDto): CategoryDto {
        return fileUploadService.createCategoryFile(fileDto)
    }

    @PutMapping("/category")
    fun updateCategory(@ModelAttribute fileDto: FileDto): CategoryDto {
        return fileUploadService.updateCategoryFile(fileDto)
    }*/
}
