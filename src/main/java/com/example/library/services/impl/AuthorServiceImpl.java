package com.example.library.services.impl;

import com.example.library.dto.authors.AuthorDto;
import com.example.library.entities.Author;
import com.example.library.repositories.AuthorRepository;
import com.example.library.services.AuthorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public Author createAuthor(AuthorDto dto) {
        log.info("Creating author: {}", dto);

        Author author = new Author();
        author.setName(dto.getName());
        author.setBio(dto.getBio());
        authorRepository.save(author);

        log.info("Author created: {}", author);
        return author;
    }

    public List<Author> getAllAuthors() {
        log.info("Retrieving all authors");

        List<Author> authors = authorRepository.findAll();

        log.info("Retrieved {} authors", authors.size());
        return authors;
    }

    public Author getAuthorById(Long id) {
        log.info("Retrieving author with id: {}", id);

        Author author = authorRepository.findById(id).orElseThrow();

        log.info("Author found: {}", author);
        return author;
    }

    public Author updateAuthor(Long id, AuthorDto dto) {
        log.info("Updating author with id: {}, new data: {}", id, dto);

        Author author = authorRepository.findById(id).orElseThrow();
        author.setName(dto.getName());
        author.setBio(dto.getBio());
        authorRepository.update(author);

        log.info("Author updated: {}", author);
        return author;
    }

    public void deleteAuthor(Long id) {
        log.info("Deleting author with id: {}", id);

        Author author = authorRepository.findById(id).orElseThrow();
        authorRepository.deleteById(id);

        log.info("Author with id {} deleted", id);
    }
}