package com.marufh.pathagar

import com.marufh.pathagar.book.repository.BookRepository
import com.marufh.pathagar.book.service.BookService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class BaseTest {

    val logger = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var bookService: BookService

    @Autowired
    lateinit var bookRepository: BookRepository
}
