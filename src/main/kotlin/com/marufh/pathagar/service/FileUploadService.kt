package com.marufh.pathagar.service

import com.marufh.pathagar.config.FileProperties
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption


@Service
class FileUploadService() {

    fun upload(file: File, path: Path, name: String): Path {
        return move(file.inputStream(), path, name)
    }

    fun upload(file: MultipartFile,  path: Path, name: String): Path {
        return move(file.inputStream, path, name)
    }

    private fun move(inputStream: InputStream, path: Path, name: String): Path {
        val finalPath = getFinalPath(path, name)
        Files.copy(inputStream, finalPath, StandardCopyOption.REPLACE_EXISTING)
        return finalPath;
    }

    private fun getFinalPath(path: Path, name: String): Path {
        val subDirectory = name.replace(".pdf", "").trim().replace("\\s+".toRegex(), "_")
        val directoryPath = path.resolve(subDirectory);
        Files.createDirectories(directoryPath);
        return directoryPath.resolve(name);
    }
}
