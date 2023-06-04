package com.marufh.pathagar.file

import com.marufh.pathagar.file.service.FileDownloadService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/public/files")
class FileController(private val fileDownloadService: FileDownloadService) {

    @GetMapping("/{path}/{subPath}/{fileName}")
    fun getFile(@PathVariable path: String, @PathVariable subPath: String, @PathVariable fileName: String ) = fileDownloadService.getFile("/$path/$subPath/$fileName")

}
