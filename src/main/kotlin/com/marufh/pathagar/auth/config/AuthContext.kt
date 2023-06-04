package com.marufh.pathagar.auth.config

import com.marufh.pathagar.auth.entity.User
import org.springframework.security.core.Authentication

// Extension functions
fun Authentication.toUser() = principal as User
