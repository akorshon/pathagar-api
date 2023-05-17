package com.marufh.pathagar.controller

import com.marufh.pathagar.service.FileDownloadService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.core.io.Resource

@RestController
@RequestMapping("/api/public/files")
class FileController(private val fileDownloadService: FileDownloadService) {

    @GetMapping("/book/{subPath}/{fileName}")
    fun getFile(@PathVariable subPath: String, @PathVariable fileName: String ): ResponseEntity<Resource> {
        return fileDownloadService.getFile("/book/${subPath}/${fileName}")
    }

}
