package com.example.library.repositories.impl;

import com.example.library.entities.Book;
import com.example.library.repositories.AuthorRepository;
import com.example.library.repositories.BookRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class BookRepositoryImpl implements BookRepository {

    private final JdbcTemplate jdbcTemplate;
    private final AuthorRepository authorRepository;

    public BookRepositoryImpl(JdbcTemplate jdbcTemplate, AuthorRepository authorRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.authorRepository = authorRepository;
    }

    private static final class BookRowMapper implements RowMapper<Book> {
        private final AuthorRepository authorRepository;

        public BookRowMapper(AuthorRepository authorRepository) {
            this.authorRepository = authorRepository;
        }

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            Book book = new Book();
            book.setId(rs.getLong("id"));
            book.setTitle(rs.getString("title"));
            Long authorId = rs.getLong("author_id");
            book.setAuthor(authorRepository.findById(authorId).orElseThrow());
            return book;
        }
    }

    @Override
    public Optional<Book> findById(Long id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new BookRowMapper(authorRepository), id));
    }

    @Override
    public List<Book> findAll() {
        String sql = "SELECT * FROM books";
        return jdbcTemplate.query(sql, new BookRowMapper(authorRepository));
    }

    @Override
    public void save(Book book) {
        String sql = "INSERT INTO books (title, author_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, book.getTitle(), book.getAuthor().getId());
    }

    @Override
    public void update(Book book) {
        String sql = "UPDATE books SET title = ?, author_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, book.getTitle(), book.getAuthor().getId(), book.getId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM books WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}