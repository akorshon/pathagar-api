package com.marufh.pathagar.service

import com.marufh.pathagar.config.FileProperties
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.PDFRenderer
import org.springframework.stereotype.Service
import java.awt.Dimension
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.File
import java.nio.file.Path
import javax.imageio.ImageIO


@Service
class PdfService(
    private val  fileProperties: FileProperties) {

    fun convertToThumb(filePath: Path, path: Path): Path {
        val file =  filePath.toFile()
        val bi = PDFRenderer(PDDocument.load(file)).renderImageWithDPI(0, 300f)
        val bookThumb: Path = path.resolve(file.nameWithoutExtension + ".jpg")
        resizeImage(bi, bookThumb.toFile(), 200, 300)
        return bookThumb
    }

    private fun resizeImage(bi: BufferedImage, file: File,  width: Int, height: Int) {
        val img = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

        val imgSize = Dimension(bi.width, bi.height)
        val boundary = Dimension(width, height)
        val dimension = getScaledDimension(imgSize, boundary)

        val x = (boundary.getWidth() - dimension!!.getWidth()).toInt() / 2
        val y = (boundary.getHeight() - dimension.getHeight()).toInt() / 2
        println("x: $x y: $y")
        img.createGraphics().drawImage(bi.getScaledInstance(dimension.width, dimension.height, Image.SCALE_SMOOTH), x, y, null)
        ImageIO.write(img, "JPEG", file)
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

        println("$newWidth, $newHeight")

        return Dimension(newWidth, newHeight)
    }


}
