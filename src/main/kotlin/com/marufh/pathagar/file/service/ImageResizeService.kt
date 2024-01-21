package com.marufh.pathagar.file.service

import com.marufh.pathagar.config.FileProperties
import com.marufh.pathagar.file.entity.FileMeta
import com.marufh.pathagar.file.entity.FileType
import com.marufh.pathagar.file.repository.FileMetaRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.awt.Dimension
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.math.BigInteger
import java.nio.file.Path
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.time.Instant
import javax.imageio.ImageIO

@Service
class ImageResizeService(
    private val fileMetaRepository: FileMetaRepository,
    private val fileProperties: FileProperties) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun createThumb(image: Path, fileType: FileType): FileMeta {
        logger.info("Creating thumbnail for image: ${image.fileName}")

        val thumbFile = image.parent.resolve( "${image.toFile().nameWithoutExtension}_thumb.jpg").toFile()
        val bi = ImageIO.read(image.toFile())
        val thumb = resize(bi, thumbFile, 200, 300)
        return FileMeta(
            name = thumb.fileName.toString(),
            fileType = fileType,
            path = getRelativePath(thumb),
            hash = getHash(thumb.toFile()),
            size = thumb.toFile().length(),
            createdAt = Instant.now()
        ).let { fileMetaRepository.save(it) }
    }

    fun resize(bi: BufferedImage, file: File, width: Int, height: Int): Path {
        logger.info("Resizing image: ${file.name}")

        val img = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val imgSize = Dimension(bi.width, bi.height)
        val boundary = Dimension(width, height)
        val dimension = getScaledDimension(imgSize, boundary)

        val x = (boundary.getWidth() - dimension!!.getWidth()).toInt() / 2
        val y = (boundary.getHeight() - dimension.getHeight()).toInt() / 2

        img.createGraphics().drawImage(bi.getScaledInstance(dimension.width, dimension.height, Image.SCALE_SMOOTH), x, y, null)
        ImageIO.write(img, "JPEG", file)
        return file.toPath()
    }

    private fun getScaledDimension(imgSize: Dimension, boundary: Dimension): Dimension? {
        val originalWidth = imgSize.width
        val originalHeight = imgSize.height
        val boundWidth = boundary.width
        val boundHeight = boundary.height

        var newWidth = originalWidth
        var newHeight = originalHeight

        // first check if we need to scale width
        if (originalWidth > boundWidth) {
            //scale width to fit
            newWidth = boundWidth
            //scale height to maintain aspect ratio
            newHeight = newWidth * originalHeight / originalWidth
        }

        // then check if we need to scale even with the new height
        if (newHeight > boundHeight) {
            //scale height to fit instead
            newHeight = boundHeight
            //scale width to maintain aspect ratio
            newWidth = newHeight * originalWidth / originalHeight
        }

        return Dimension(newWidth, newHeight)
    }

    private fun getRelativePath(filePath: Path): String {
        return Path.of(fileProperties.base).relativize(filePath).toString()
    }

    private fun getHash(file: File): String {
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
