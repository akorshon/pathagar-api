package com.marufh.pathagar.file

import com.marufh.pathagar.file.dto.FileDto
import com.marufh.pathagar.file.service.FileUploadService
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin/file")
class FileAdminController(private val fileUploadService: FileUploadService) {

    @PostMapping("/book")
    fun uploadBook(@ModelAttribute fileDto: FileDto) = fileUploadService.uploadBook(fileDto)

    @PostMapping("/author")
    fun uploadAuthor(@ModelAttribute fileDto: FileDto) = fileUploadService.uploadAuthor(fileDto)
}
