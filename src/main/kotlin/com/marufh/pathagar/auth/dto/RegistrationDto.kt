package com.marufh.pathagar.auth.dto

data class RegistrationDto(
    val email: String,
    val password: String,
    val confirmPassword: String
)
