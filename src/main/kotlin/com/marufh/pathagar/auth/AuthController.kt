package com.marufh.pathagar.auth

import com.marufh.pathagar.auth.dto.LoginDto
import com.marufh.pathagar.auth.dto.TokenDto
import com.marufh.pathagar.auth.entity.Role
import com.marufh.pathagar.auth.entity.User
import com.marufh.pathagar.auth.service.HashService
import com.marufh.pathagar.auth.service.TokenService
import com.marufh.pathagar.auth.service.UserService
import com.marufh.pathagar.exception.NotFoundException
import com.marufh.pathagar.exception.UnauthorizedException
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val hashService: HashService,
    private val userService: UserService,
    private val tokenService: TokenService) {


    @PostMapping("/login")
    fun login(@RequestBody login: LoginDto): TokenDto {
        println("login: $login")
        val user = userService.findByEmail(login.email)?: throw NotFoundException("Invalid username or password")
        if (!hashService.checkBcrypt(login.password, user.password)) {
            throw NotFoundException("Invalid username or password")
        }
        return TokenDto(tokenService.generateToken(user))
    }

    @PostMapping("/registration")
    fun register(@RequestBody login: LoginDto): TokenDto {

        val user = userService.findByEmail(login.email)
        if (user != null) {
            throw NotFoundException("User already exists")
        }
        val newUser = userService.save(User(login.email, hashService.hashBcrypt(login.password), setOf(Role.ROLE_USER)))
        return TokenDto(tokenService.generateToken(newUser))
    }
}
