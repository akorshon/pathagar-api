package com.marufh.pathagar.auth

import com.marufh.pathagar.BaseTest
import org.junit.jupiter.api.Test

class UserServiceTest: BaseTest() {

    @Test
    fun `test find by email`() {
        val user = userRepository.save(getUser())
        val foundUser = userService.findByEmail(user.email)
        assert(foundUser?.email == user.email)
    }

    @Test
    fun `test save user`() {
        val user = getUser()
        val savedUser = userService.save(user)
        assert(savedUser.email == user.email)
    }

    @Test
    fun `test find by id`() {
        val user = userRepository.save(getUser())
        val foundUser = userService.findById(user.id!!)
        assert(foundUser?.email == user.email)
    }
}