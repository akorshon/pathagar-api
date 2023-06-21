package com.marufh.pathagar.file.service

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.PDFRenderer
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Path


@Service
class PdfService(private val imageResizeService: ImageResizeService) {

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
        var bookThumb: Path = path
        try {
            val file =  filePath.toFile()
            val pd = PDDocument.load(file)
            val bi = PDFRenderer(PDDocument.load(file)).renderImageWithDPI(page, 300f)
            bookThumb = path.resolve(file.nameWithoutExtension + ".jpg")
            imageResizeService.resize(bi, bookThumb.toFile(), 200, 300)
            pd.close()
        } catch (ex: Exception) {
            logger.error("Fail to convert pdf thumb: $filePath")
        }
        return bookThumb;
    }
}
