package com.marufh.pathagar.dev

import com.marufh.pathagar.auth.entity.Role
import com.marufh.pathagar.auth.entity.User
import com.marufh.pathagar.auth.repository.UserRepository
import com.marufh.pathagar.auth.service.HashService
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("dev")
class InitData(
    val hashService: HashService,
    val userRepository: UserRepository): ApplicationRunner {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun run(args: ApplicationArguments?) {
        logger.info("Initializing data for dev profile")

        val email = "admin@gmail.com";

        userRepository.findByEmail(email)?.let {
                logger.info("User already exists with email: {}", email)
        } ?: run {
            User(
                email = "admin@gmail.com",
                password = hashService.hashBcrypt("123456"),
                roles = setOf(Role.ROLE_ADMIN)
            ).let { userRepository.save(it) }
        }
    }
}
