package com.marufh.pathagar.file.service

import com.marufh.pathagar.config.FileProperties
import com.marufh.pathagar.exception.NotFoundException
import org.slf4j.LoggerFactory
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.http.CacheControl
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.nio.file.Path
import java.util.concurrent.TimeUnit

@Service
class FileDownloadService(private val fileProperties: FileProperties) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun getFile(path: String): ResponseEntity<Resource> {
        logger.info("Downloading file: $path")

        return Path.of(fileProperties.base, path).run {
            if(toFile().exists()) {
                ResponseEntity.status(HttpStatus.OK)
                    .cacheControl(CacheControl.maxAge(7, TimeUnit.DAYS))
                    .body(UrlResource(toUri()))
            }
            else {
                throw NotFoundException("File not found: $path")
            }
        }
    }

}
