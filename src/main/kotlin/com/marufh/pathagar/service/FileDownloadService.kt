package com.marufh.pathagar.service

import com.marufh.pathagar.config.FileProperties
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.http.CacheControl
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.nio.file.Path
import java.util.concurrent.TimeUnit

@Service
class FileDownloadService(
    private val fileProperties: FileProperties) {

    fun getFile(path: String): ResponseEntity<Resource> {
        val filePath = Path.of(fileProperties.base + path)
        val resource = UrlResource(filePath.toUri())
        return ResponseEntity.status(HttpStatus.OK)
            .cacheControl(CacheControl.maxAge(365, TimeUnit.DAYS))
            .body(resource)
    }

}
