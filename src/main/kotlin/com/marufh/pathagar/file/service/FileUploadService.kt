package com.marufh.pathagar.file.service

import com.marufh.pathagar.config.FileProperties
import com.marufh.pathagar.exception.AlreadyExistException
import com.marufh.pathagar.file.dto.FileDto
import com.marufh.pathagar.file.entity.FileMeta
import com.marufh.pathagar.file.entity.FileType
import com.marufh.pathagar.file.repository.FileMetaRepository
import jakarta.persistence.EntityNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.math.BigInteger
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.time.Instant
import kotlin.io.path.pathString


@Service
class FileUploadService(
    private val fileProperties: FileProperties,
    private val fileMetaRepository: FileMetaRepository) {

    private val logger = LoggerFactory.getLogger(javaClass)

    private val fileNameRegex = "\\.[^/.]+$"

    fun createFile(fileDto: FileDto): FileMeta {
        logger.info("Uploading  file: ${fileDto.file.originalFilename}")

        val filePath = filePath(fileDto)
        val file = filePath.toFile()
        val hash = getHash(file)
        val size = file.length()

        fileMetaRepository.findByHash(hash!!)?.let {
            throw AlreadyExistException("File already exist")
        }

       return FileMeta(
            name = fileDto.name,
            fileType = fileDto.fileType,
            path = getRelativePath(filePath),
            hash = hash,
            size = size,
            createdAt = Instant.now()
        ).let { fileMetaRepository.save(it) }
    }

    fun updateFile(fileDto: FileDto): FileMeta  {
        logger.info("Updating file: ${fileDto.file.originalFilename}")

        val fileMeta = fileMetaRepository.findById(fileDto.id!!)
            .orElseThrow() { EntityNotFoundException("File not found with id: ${fileDto.id}") }

        val filePath = filePath(fileDto)
        val file = filePath.toFile()
        val hash = getHash(file)
        val size = file.length()

        fileMetaRepository.findByHash(hash!!)?.run {
            logger.info("File already exist with hash: $hash")
            throw AlreadyExistException("Book already exist")
        }

        // remove old file
        Files.delete(Path.of(fileProperties.base +"/"+ fileMeta.path))

        fileMeta.name = fileDto.name
        fileMeta.path = getRelativePath(filePath)
        fileMeta.hash = hash
        fileMeta.size = size
        fileMeta.createdAt = Instant.now()

        return fileMetaRepository.save(fileMeta)
    }

    fun upload(file: MultipartFile,  path: Path, name: String) = move(file.inputStream, path, name)

    private fun filePath(fileDto: FileDto) = when(fileDto.fileType) {
        FileType.BOOK -> upload(fileDto.file, Path.of(fileProperties.book), fileDto.file.originalFilename!!)
        FileType.BOOK_THUMB -> TODO()
        FileType.AUTHOR ->  upload(fileDto.file, Path.of(fileProperties.author), fileDto.file.originalFilename!!)
        FileType.AUTHOR_THUMB -> TODO()
        FileType.CATEGORY ->  upload(fileDto.file, Path.of(fileProperties.category), fileDto.file.originalFilename!!)
        FileType.CATEGORY_THUMB -> TODO()
    }

    private fun move(inputStream: InputStream, path: Path, name: String) = getFinalPath(path, name).apply {
        Files.copy(inputStream, this, StandardCopyOption.REPLACE_EXISTING)
    }

    private fun getFinalPath(path: Path, name: String): Path {
        val subDirectory = name.replace("\\.[^/.]+\$".toRegex(), "").trim().replace("\\s+".toRegex(), "_")
        return path.resolve(subDirectory).apply { Files.createDirectories(this) }.resolve(name);
    }

    private fun getRelativePath(filePath: Path): String {
        return Path.of(fileProperties.base).relativize(filePath).toString()
    }

    private fun getHash(file: File): String? {
        return try {
            val fi = FileInputStream(file)
            val fileData = ByteArray(file.length().toInt())
            fi.read(fileData)
            fi.close()
            BigInteger(1, messageDigest.digest(fileData)).toString(16)
        } catch (e: IOException) {
            throw java.lang.RuntimeException(e)
        }
    }

    companion object {
        var messageDigest: MessageDigest
        init {
            try {
                messageDigest = MessageDigest.getInstance("SHA-512");
            } catch (e: NoSuchAlgorithmException) {
                throw RuntimeException ("cannot initialize SHA-512 hash function", e);
            }
        }
    }
}
