package com.marufh.pathagar.auth.service

import com.marufh.pathagar.auth.entity.User
import com.marufh.pathagar.auth.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository) {

    fun findByEmail(email: String) = userRepository.findByEmail(email)

    fun save(user: User) = userRepository.save(user)

    fun findById(id: String): User? = userRepository.findByIdOrNull(id)
}
