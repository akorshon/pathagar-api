package com.marufh.pathagar.controller.admin

import com.marufh.pathagar.dto.FileDto
import com.marufh.pathagar.service.FileUploadService
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin/files")
class FileAdminController(private val fileUploadService: FileUploadService) {

    @PostMapping
    fun uploadFile(@ModelAttribute fileDto: FileDto): Map<String, String> {
        val path = fileUploadService.upload(fileDto)
        return mapOf("filePath" to  path)
    }
}
