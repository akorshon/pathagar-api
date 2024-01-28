package com.marufh.pathagar.file

import com.marufh.pathagar.BaseTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.awt.image.BufferedImage
import java.io.File
import java.nio.file.Files
import javax.imageio.ImageIO


class ImageResizeServiceTest: BaseTest() {

    @Test
    fun `test resize image`() {
        logger.info("Testing resize image")

        val imagePath = imageResizeService.resize(
            ImageIO.read(File("src/test/resources/test-author.jpg")),
            File("src/test/resources/resized-author.jpg"),
            100, 100
        )

        val bimg: BufferedImage = ImageIO.read(File(imagePath.toString()))
        assertTrue(Files.exists(imagePath))
        assertEquals(100, bimg.width)
        assertEquals(100, bimg.height)
    }
}
