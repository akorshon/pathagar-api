package com.marufh.pathagar.file.service

import com.marufh.pathagar.config.FileProperties
import com.marufh.pathagar.file.entity.FileMeta
import com.marufh.pathagar.file.entity.FileType
import com.marufh.pathagar.file.repository.FileMetaRepository
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.PDFRenderer
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.math.BigInteger
import java.nio.file.Path
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.time.Instant


@Service
class PdfService(
    private val fileMetaRepository: FileMetaRepository,
    private val fileProperties: FileProperties,
    private val imageResizeService: ImageResizeService) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun convertToThumb(filePath: Path, path: Path): Path {
        logger.info("Converting pdf to thumb: $filePath")
       return convertToThumbFromPage(filePath, path, 0);
    }

    fun getTotalPage(file:File): Int {
        return PDDocument.load(file).numberOfPages;
    }


    fun convertToThumbFromPage(filePath: Path, path: Path, page: Int): Path {
        logger.info("Converting pdf to thumb: $filePath, page: $page")

        val file =  filePath.toFile()
        val pd = PDDocument.load(file)
        val bi = PDFRenderer(PDDocument.load(file)).renderImageWithDPI(page, 300f)
        val bookThumb = path.resolve(file.nameWithoutExtension + ".jpg")
        imageResizeService.resize(bi, bookThumb.toFile(), 200, 300)
        pd.close()
        return bookThumb;
    }

    fun createCoverImage(pdfFilePath: Path): FileMeta {

        val path = convertToThumbFromPage(pdfFilePath, pdfFilePath.parent, 0);
        return fileMetaRepository.save(FileMeta(
            name = path.fileName.toString(),
            fileType = FileType.BOOK,
            path = getRelativePath(path),
            hash = getHash(path.toFile())!!,
            size = path.toFile().length(),
            createdAt = Instant.now()
        ))
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
