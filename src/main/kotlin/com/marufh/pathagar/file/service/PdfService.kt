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
import java.nio.file.Path
import java.time.Instant


@Service
class PdfService(
    private val fileService: FileService,
    private val fileMetaRepository: FileMetaRepository,
    private val fileProperties: FileProperties,
    private val imageResizeService: ImageResizeService) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun getTotalPage(file:File): Int {
        return PDDocument.load(file).numberOfPages
    }


    fun convertToThumbFromPage(filePath: Path, path: Path, page: Int): Path {
        logger.info("Converting pdf to thumb: $filePath, page: $page")

        val file =  filePath.toFile()
        val pd = PDDocument.load(file)
        val bi = PDFRenderer(PDDocument.load(file)).renderImageWithDPI(page, 300f)
        val bookThumb = path.resolve(file.nameWithoutExtension + ".jpg")
        imageResizeService.resize(bi, bookThumb.toFile(), 200, 300)
        pd.close()
        return bookThumb
    }

    fun createCoverImage(pdfFilePath: Path, fileType: FileType): FileMeta {

        val path = convertToThumbFromPage(pdfFilePath, pdfFilePath.parent, 0)
        return fileMetaRepository.save(FileMeta(
            name = path.fileName.toString(),
            fileType = fileType,
            path = getRelativePath(path),
            hash = fileService.getHash(path.toFile()),
            size = path.toFile().length(),
            createdAt = Instant.now()
        ))
    }

    private fun getRelativePath(filePath: Path): String {
        return Path.of(fileProperties.base).relativize(filePath).toString()
    }

}
