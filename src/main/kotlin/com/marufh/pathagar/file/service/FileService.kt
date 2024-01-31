package com.marufh.pathagar.file.service

import com.marufh.pathagar.config.FileProperties
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.math.BigInteger
import java.nio.file.Files
import java.nio.file.Path
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

@Service
class FileService(
    private val fileProperties: FileProperties) {

    fun deleteFiles(imagePath: String, thumbPath: String) {
        Files.delete(Path.of(fileProperties.base, imagePath))
        Files.delete(Path.of(fileProperties.base,thumbPath))
        Files.delete(Path.of(fileProperties.base, imagePath).parent)
    }

    fun getHash(file: File): String {
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
                messageDigest = MessageDigest.getInstance("SHA-512")
            } catch (e: NoSuchAlgorithmException) {
                throw RuntimeException ("cannot initialize SHA-512 hash function", e)
            }
        }
    }
}