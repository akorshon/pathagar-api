package com.marufh.pathagar.service

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.awt.Dimension
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import javax.imageio.ImageIO

@Service
class FileUploadService {

    fun upload(file: File, path: Path, name: String) = move(file.inputStream(), path, name)

    fun upload(file: MultipartFile,  path: Path, name: String) = move(file.inputStream, path, name)

    fun resizeImage(bi: BufferedImage, file: File, width: Int, height: Int): Path {
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
            println("Fit width")
            //scale width to fit
            newWidth = boundWidth
            //scale height to maintain aspect ratio
            newHeight = newWidth * originalHeight / originalWidth
        }

        // then check if we need to scale even with the new height
        if (newHeight > boundHeight) {
            println("Fit height")
            //scale height to fit instead
            newHeight = boundHeight
            //scale width to maintain aspect ratio
            newWidth = newHeight * originalWidth / originalHeight
        }

        return Dimension(newWidth, newHeight)
    }

    private fun move(inputStream: InputStream, path: Path, name: String) = getFinalPath(path, name)
        .apply { Files.copy(inputStream, this, StandardCopyOption.REPLACE_EXISTING) }

    private fun getFinalPath(path: Path, name: String): Path {
        val subDirectory = name.replace("\\.[^/.]+\$".toRegex(), "").trim().replace("\\s+".toRegex(), "_")
        return path.resolve(subDirectory).apply { Files.createDirectories(this) }.resolve(name);
    }
}
