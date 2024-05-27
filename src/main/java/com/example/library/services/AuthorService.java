package com.example.library.services;

import com.example.library.dto.authors.AuthorDto;
import com.example.library.entities.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {

    Author createAuthor(AuthorDto dto);
    List<Author> getAllAuthors();
    Author getAuthorById(Long id);
    Author updateAuthor(Long id, AuthorDto dto);
    boolean deleteAuthor(Long id);
}
