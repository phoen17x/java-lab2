package com.example.library.repositories.impl;

import com.example.library.entities.Author;
import com.example.library.repositories.AuthorRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class AuthorRepositoryImpl implements AuthorRepository {

    private final JdbcTemplate jdbcTemplate;

    public AuthorRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final class AuthorRowMapper implements RowMapper<Author> {
        @Override
        public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
            Author author = new Author();
            author.setId(rs.getLong("id"));
            author.setName(rs.getString("name"));
            author.setBio(rs.getString("bio"));
            return author;
        }
    }

    @Override
    public Optional<Author> findById(Long id) {
        String sql = "SELECT * FROM authors WHERE id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new AuthorRowMapper(), id));
    }

    @Override
    public List<Author> findAll() {
        String sql = "SELECT * FROM authors";
        return jdbcTemplate.query(sql, new AuthorRowMapper());
    }

    @Override
    public void save(Author author) {
        String sql = "INSERT INTO authors (name, bio) VALUES (?, ?)";
        jdbcTemplate.update(sql, author.getName(), author.getBio());
    }

    @Override
    public void update(Author author) {
        String sql = "UPDATE authors SET name = ?, bio = ? WHERE id = ?";
        jdbcTemplate.update(sql, author.getName(), author.getBio(), author.getId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM authors WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
