package com.marufh.pathagar.auth.repository

import com.marufh.pathagar.auth.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserRepository : JpaRepository<User, String> {

    @Query("SELECT u FROM User u WHERE u.email = ?1")
    fun findByName(email: String): User?
}
