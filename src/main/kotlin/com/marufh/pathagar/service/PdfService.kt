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
    private val fileUploadService: FileUploadService) {

    fun convertToThumb(filePath: Path, path: Path): Path {
        val file =  filePath.toFile()
        val bi = PDFRenderer(PDDocument.load(file)).renderImageWithDPI(0, 300f)
        val bookThumb: Path = path.resolve(file.nameWithoutExtension + ".jpg")
        fileUploadService.resizeImage(bi, bookThumb.toFile(), 200, 300)
        return bookThumb
    }

}
