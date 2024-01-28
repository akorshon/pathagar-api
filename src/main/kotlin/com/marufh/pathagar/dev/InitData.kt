package com.marufh.pathagar.dev

import com.marufh.pathagar.auth.entity.Role
import com.marufh.pathagar.auth.entity.User
import com.marufh.pathagar.auth.repository.UserRepository
import com.marufh.pathagar.auth.service.HashService
import com.marufh.pathagar.author.repository.AuthorRepository
import com.marufh.pathagar.book.repository.BookRepository
import com.marufh.pathagar.category.model.CategoryRepository
import com.marufh.pathagar.config.FileProperties
import com.marufh.pathagar.file.repository.FileMetaRepository
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("dev")
class InitData(
    val categoryRepository: CategoryRepository,
    val authorRepository: AuthorRepository,
    val bookRepository: BookRepository,
    val fileMetaRepository: FileMetaRepository,
    val fileProperties: FileProperties,
    val hashService: HashService,
    val userRepository: UserRepository): ApplicationRunner {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun run(args: ApplicationArguments?) {
        logger.info("Initializing data for dev profile")

        if(userRepository.findAll().isEmpty()) {
            User(
                email = "admin@gmail.com",
                password = hashService.hashBcrypt("123456"),
                roles = setOf(Role.ROLE_ADMIN)
            ).let { userRepository.save(it) }

            User(
                email = "user@gmail.com",
                password = hashService.hashBcrypt("123456"),
                roles = setOf(Role.ROLE_USER)
            ).let { userRepository.save(it) }

        }
    }

}
