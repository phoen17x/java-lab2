package com.example.library.controllers;

import com.example.library.dto.authors.AuthorDto;
import com.example.library.entities.Author;
import com.example.library.repositories.AuthorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void seedDatabase() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "user_roles", "users", "roles", "books", "authors");
        jdbcTemplate.execute("ALTER SEQUENCE public.users_id_seq RESTART");
        jdbcTemplate.execute("ALTER SEQUENCE public.roles_id_seq RESTART");
        jdbcTemplate.execute("ALTER SEQUENCE public.books_id_seq RESTART");
        jdbcTemplate.execute("ALTER SEQUENCE public.authors_id_seq RESTART");

        Author author1 = new Author();
        author1.setName("John Doe");
        author1.setBio("John Doe's biography");
        authorRepository.save(author1);

        Author author2 = new Author();
        author2.setName("Jane Doe");
        author2.setBio("Jane Doe's biography");
        authorRepository.save(author2);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateAuthor() throws Exception {
        AuthorDto authorDto = new AuthorDto("John Doe", "John Doe's biography");
        mockMvc.perform(post("/api/v1/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.bio").value("John Doe's biography"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetAllAuthors() throws Exception {
        mockMvc.perform(get("/api/v1/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2))); // Assuming there are 2 authors in the database
    }

    @Test
    @WithMockUser(roles = "USER")
    void testGetAuthorById() throws Exception {
        mockMvc.perform(get("/api/v1/authors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.bio").value("John Doe's biography"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateAuthor() throws Exception {
        AuthorDto authorDto = new AuthorDto("John Doe Updated", "John Doe's updated biography");
        mockMvc.perform(put("/api/v1/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe Updated"))
                .andExpect(jsonPath("$.bio").value("John Doe's updated biography"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteAuthor() throws Exception {
        mockMvc.perform(delete("/api/v1/authors/1"))
                .andExpect(status().isNoContent());
    }
}