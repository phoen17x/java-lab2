package com.example.library.repositories;

import com.example.library.entities.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {
    Optional<Author> findById(Long id);
    List<Author> findAll();
    void save(Author author);
    void update(Author author);
    void deleteById(Long id);
}