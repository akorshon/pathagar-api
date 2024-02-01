package com.marufh.pathagar.author

import com.fasterxml.jackson.core.type.TypeReference
import com.marufh.pathagar.BaseTest
import com.marufh.pathagar.RestPage
import com.marufh.pathagar.author.dto.AuthorCreateRequest
import com.marufh.pathagar.author.dto.AuthorDto
import com.marufh.pathagar.author.entity.Author
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID

class CategoryControllerTest: BaseTest() {

    @Test
    @WithMockUser(username = "user1", password = "pwd", roles = arrayOf("USER"))
    fun `should return all authors`() {
        logger.info("should return all authors")

        // Given
        bookRepository.deleteAll()
        authorRepository.deleteAll()
        listOf(
            Author(name = "test author${UUID.randomUUID()}", description = "test description"),
            Author(name = "test author${UUID.randomUUID()}", description = "test description"),
            Author(name = "test author${UUID.randomUUID()}", description = "test description"),
            Author(name = "test author${UUID.randomUUID()}", description = "test description"),
            Author(name = "test author${UUID.randomUUID()}", description = "test description"),
        ).map { authorRepository.save(it) }

        // When
        val authorPage = mockMvc.perform(get(AUTHOR_URL))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn().response
            .contentAsString.let {
                objectMapper.readValue(it, object : TypeReference<RestPage<AuthorDto>>() {})
            }

        // Then
        assert(authorPage.totalElements == 5L)
    }

    @Test
    @WithMockUser(username = "user1", password = "pwd", roles = arrayOf("USER"))
    fun `should return author details`() {
        logger.info("should return author details")

        // Given
        val authorEntity = authorRepository.save(Author(name = "test author${UUID.randomUUID()}", description = "test description"))

        // When
        val author = mockMvc.perform(get("$AUTHOR_URL/{id}", authorEntity.id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn().response
            .contentAsString.let { objectMapper.readValue(it, AuthorDto::class.java) }

        // Then
        assert(author.id == authorEntity.id)
        assert(author.name == authorEntity.name)
        assert(author.description == authorEntity.description)

    }

}
