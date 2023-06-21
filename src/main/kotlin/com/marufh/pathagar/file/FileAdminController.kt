package com.marufh.pathagar.file

import com.marufh.pathagar.file.dto.FileDto
import com.marufh.pathagar.file.service.FileUploadService
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/admin/file")
class FileAdminController(private val fileUploadService: FileUploadService) {

    @PostMapping("/book")
    fun uploadBook(@ModelAttribute fileDto: FileDto) = fileUploadService.createBookFile(fileDto)

    @PutMapping("/book/{id}")
    fun updateBook(@PathVariable id: String,  @RequestParam("file") multipartFile: MultipartFile) = fileUploadService.updateBookFile(id, multipartFile)

    @PostMapping("/author")
    fun uploadAuthor(@ModelAttribute fileDto: FileDto) = fileUploadService.createAuthorFile(fileDto)

    @PutMapping("/author/{id}")
    fun updateAuthor(@PathVariable id: String, @ModelAttribute fileDto: FileDto) = fileUploadService.updateUploadAuthor(id, fileDto)
}
