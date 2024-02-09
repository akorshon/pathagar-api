package com.marufh.pathagar.auth.config

import com.marufh.pathagar.auth.RestAuthenticationEntryPoint
import com.marufh.pathagar.auth.service.TokenService
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val tokenService: TokenService,
    private val authenticationEntryPoint: RestAuthenticationEntryPoint) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        logger.info("Configuring security filter chain")


            http
                .csrf { it.disable() }
                .exceptionHandling { it.authenticationEntryPoint(authenticationEntryPoint) }
                .cors { it.configurationSource(corsConfigurationSource()) }
                .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
                .authorizeHttpRequests { it
                    .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/auth/registration").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/public/**").permitAll()
                    .requestMatchers("/api/admin/**").hasRole("ADMIN")
                    .requestMatchers("/api/user/**").hasAnyRole( "USER", "ADMIN")
                    .anyRequest().authenticated()
                }
            .oauth2ResourceServer { it.jwt { } }
            .authenticationManager { auth ->
                val jwt = auth as BearerTokenAuthenticationToken
                val user = tokenService.parseToken(jwt.token)?: throw InvalidBearerTokenException("Invalid token")
                UsernamePasswordAuthenticationToken(user, null, listOf(user.roles.map { SimpleGrantedAuthority(it.name) }).flatten())
            }
        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        logger.info("Configuring cors")

        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("*")
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE")
        configuration.allowedHeaders = listOf("authorization", "content-type")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}
