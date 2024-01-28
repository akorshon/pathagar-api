package com.marufh.pathagar.auth

import com.marufh.pathagar.BaseTest
import org.junit.jupiter.api.Test

class HashServiceTest: BaseTest() {

    @Test
    fun `test bcrypt hash`() {
        val hash = hashService.hashBcrypt("password")
        assert(hashService.checkBcrypt("password", hash))
    }
}