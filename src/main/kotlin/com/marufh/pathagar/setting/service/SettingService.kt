package com.marufh.pathagar.setting.service

import com.marufh.pathagar.config.FileProperties
import com.marufh.pathagar.file.entity.FileMeta
import com.marufh.pathagar.file.repository.FileMetaRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileInputStream
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


@Service
class SettingService(
    private val fileProperties: FileProperties,
    private val fileMetaRepository: FileMetaRepository) {

    private val logger = LoggerFactory.getLogger(javaClass)

    /*fun generateHash() {
        logger.info("Generating hash is started ")

        var pageRequest = PageRequest.of(0, 500)
        var onePage: Page<FileMeta> = fileMetaRepository.findAll(pageRequest)
        while (!onePage.isEmpty) {
            pageRequest = pageRequest.next()
            for (fileMeta in onePage.getContent()) {
                if (fileMeta.hash.isNotEmpty()) {
                    continue
                }
                logger.info("Generating hash for: {}", fileMeta.name)
                try {
                    val file = File(fileProperties.base +"/"+ fileMeta.path)
                    val fi = FileInputStream(file)
                    val fileData = ByteArray(file.length().toInt())
                    fi.read(fileData)
                    fi.close()
                    val hash: String = BigInteger(1, messageDigest.digest(fileData)).toString(16)
                    fileMeta.hash = hash
                    fileMeta.size = file.length()
                    fileMetaRepository.save(fileMeta)
                } catch (e: Exception) {
                    logger.warn("Error generating hash: {}", e.message)
                }
            }
            onePage = fileMetaRepository.findAll(pageRequest)
        }
        logger.info("Generating hash is finished")
    }*/

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
