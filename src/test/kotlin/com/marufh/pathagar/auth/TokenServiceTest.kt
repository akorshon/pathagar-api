package com.marufh.pathagar.auth

import com.marufh.pathagar.BaseTest
import org.junit.jupiter.api.Test

class TokenServiceTest: BaseTest() {

    @Test
    fun `test jwt token generation`() {

        // Given
        val user = getUser()
        userRepository.save(user)


        // When
        val token = tokenService.generateToken(user)
        val tokenUser = tokenService.parseToken(token)

        // Then
        assert(token.isNotEmpty())
        assert(tokenUser?.email == user.email)
        assert(tokenUser?.roles?.size == user.roles.size)
        assert(tokenUser?.roles?.first()?.name == user.roles.first().name)
    }

}