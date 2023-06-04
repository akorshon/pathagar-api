package com.marufh.pathagar.file.service

import com.marufh.pathagar.author.dto.AuthorDto
import com.marufh.pathagar.author.entity.Author
import com.marufh.pathagar.author.service.AuthorService
import com.marufh.pathagar.book.dto.BookDto
import com.marufh.pathagar.book.entity.Book
import com.marufh.pathagar.book.repository.BookRepository
import com.marufh.pathagar.book.service.BookService
import com.marufh.pathagar.config.FileProperties
import com.marufh.pathagar.exception.AlreadyExistException
import com.marufh.pathagar.file.dto.FileDto
import com.marufh.pathagar.file.entity.FileType
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
import javax.imageio.ImageIO


@Service
class FileUploadService(
    private val bookService: BookService,
    private val imageService: ImageService,
    private val authorService: AuthorService,
    private val bookRepository: BookRepository,
    private val pdfService: PdfService,
    private val fileProperties: FileProperties ) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun uploadBook(fileDto: FileDto): Book  {
        logger.info("Uploading book file: ${fileDto.file.originalFilename}")

        val name =  fileDto.file.originalFilename?.replace("\\.[^/.]+$".toRegex(), "")?.replace("_".toRegex(), " ")
        val filePath = upload(fileDto.file, Path.of(fileProperties.book))
        val file = filePath.toFile()
        val hash = getHash(file)

        bookRepository.findByHash(hash!!)?.run {
            logger.info("Book already exist with hash: $hash")
            throw AlreadyExistException("Book already exist with hash: $hash")
        }

        val coverImage = pdfService.convertToThumbFromPage(filePath, filePath.parent, 0)
        val size = file.length()
        val totalPage = pdfService.getTotalPage(file)

        val book = BookDto(
            name = name!!,
            description = "",
            filePath = getRelativePath(filePath),
            coverImage = getRelativePath(coverImage),
            deleted = false,
            fileType = FileType.BOOK,
            hash = hash,
            size = size,
            totalPage = totalPage,
            coverImagePage = 0,
        )
        return bookService.create(book)
    }

    fun uploadAuthor(fileDto: FileDto): Author  {
        logger.info("Uploading author file: ${fileDto.file.originalFilename}")

        val authorName =  fileDto.file.originalFilename?.replace("\\.[^/.]+$".toRegex(), "")?.replace("_", " ")
        val image = upload(fileDto.file, Path.of(fileProperties.author))
        val thumbnail = imageService.resizeImage(ImageIO.read(image.toFile()), image.parent.resolve( "${authorName}_thumb.jpg").toFile(), 200, 300)

        val authorDto = AuthorDto(
            name = authorName!!,
            description = "",
            image = getRelativePath(image),
            thumbnail = getRelativePath(thumbnail),
        )

        return authorService.create(authorDto)
}

    fun upload(file: MultipartFile,  path: Path) = move(file.inputStream, path, file.originalFilename!!)

    private fun move(inputStream: InputStream, path: Path, name: String) = getFinalPath(path, name).apply {
        Files.copy(inputStream, this, StandardCopyOption.REPLACE_EXISTING)
    }

    private fun getFinalPath(path: Path, name: String): Path {
        val subDirectory = name.replace("\\.[^/.]+\$".toRegex(), "").trim().replace("\\s+".toRegex(), "_")
        return path.resolve(subDirectory).apply { Files.createDirectories(this) }.resolve(name);
    }

    fun updateThumb(bookId: String, page: Int): Book {
        logger.info("Updating book thumb: $bookId, page: $page")

        val book = bookRepository.findById(bookId)
            .orElseThrow { EntityNotFoundException("Book not found with id: $bookId") }

        val path = Path.of(fileProperties.base +"/"+ book.filePath)
        val thumbPath = pdfService.convertToThumbFromPage(path, path.parent, page)

        book.coverImagePage = page
        book.coverImage = getRelativePath(thumbPath)
        return bookRepository.save(book)
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
