package com.marufh.pathagar.auth.service

import com.marufh.pathagar.auth.entity.User
import org.slf4j.LoggerFactory
import org.springframework.security.oauth2.jwt.JwsHeader
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class TokenService(
    val jwtEncoder: JwtEncoder,
    val jwtDecoder: JwtDecoder,
    val usrService: UserService) {

    private val logger = LoggerFactory.getLogger(javaClass)


    fun generateToken(user: User): String {
        logger.info("Generating token for user {}", user.email)

        val jwsHeader = JwsHeader.with {"HS256"}.build()
        val claims = JwtClaimsSet.builder()
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plus(2, ChronoUnit.DAYS))
            .subject(user.email)
            .claim("roles", user.roles)
            .build()
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).tokenValue
    }


    fun parseToken(token: String): User? {
        //logger.info("Parsing token {}", token)

        return try {
            val jwt = jwtDecoder.decode(token)
            val email = jwt.subject
            usrService.findByEmail(email)
        } catch (e: Exception) {
            logger.error("Fail to parse token {}", e.message)
            null
        }
    }
}
