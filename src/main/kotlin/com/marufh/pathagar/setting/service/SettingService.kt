package com.marufh.pathagar.setting.service

import com.marufh.pathagar.book.entity.Book
import com.marufh.pathagar.book.repository.BookRepository
import com.marufh.pathagar.config.FileProperties
import com.marufh.pathagar.file.service.PdfService
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

    private val pdfService: PdfService,
    private val bookRepository: BookRepository,
    private val fileProperties: FileProperties, ) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun generateHash() {
        logger.info("Generating hash is started ")

        var pageRequest = PageRequest.of(0, 500)
        var onePage: Page<Book> = bookRepository.findAll(pageRequest)
        while (!onePage.isEmpty()) {
            pageRequest = pageRequest.next()
            for (book in onePage.getContent()) {
                /*if (book.hash != null) {
                    continue
                }*/
                logger.info("Generating hash for: {}", book.name)
                try {
                    val file = File(fileProperties.base +"/"+ book.pdfFile?.path)
                    val fi = FileInputStream(file)
                    val fileData = ByteArray(file.length().toInt())
                    fi.read(fileData)
                    fi.close()
                    val hash: String = BigInteger(1, messageDigest.digest(fileData)).toString(16)
                    //book.hash = hash
                    book.totalPage = pdfService.getTotalPage(file)
                    //book.size = file.length()
                    bookRepository.save(book)
                } catch (e: Exception) {
                    logger.warn("Error generating hash: {}", e.message)
                }
            }
            onePage = bookRepository.findAll(pageRequest)
        }
        logger.info("Generating hash is finished")
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
