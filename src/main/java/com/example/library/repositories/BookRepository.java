package com.example.library.repositories;

import com.example.library.entities.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Optional<Book> findById(Long id);
    List<Book> findAll();
    void save(Book book);
    void update(Book book);
    void deleteById(Long id);
}