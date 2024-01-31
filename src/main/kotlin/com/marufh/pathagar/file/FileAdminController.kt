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
}
