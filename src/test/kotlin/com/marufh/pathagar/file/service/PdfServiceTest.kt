package com.marufh.pathagar.file.service

import com.marufh.pathagar.BaseTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Path

class PdfServiceTest: BaseTest() {

    @Test
    fun `test convert pdf to image`() {
        logger.info("Testing convert pdf to image")

        // Given
        val file =  File("src/test/resources/test-book.pdf").toPath()
        val thumbPath = File("src/test/resources/test-book.jpg").toPath()

        // When
        val path = pdfService.convertToThumbFromPage(file, thumbPath, 0)


        assertNotNull(path)
    }

    @Test
    fun `test pdf total page`() {
        logger.info("Testing pdf page size")

        val file =  File("src/test/resources/test-book.pdf")
        assertEquals(2, pdfService.getTotalPage(file))
    }
}
