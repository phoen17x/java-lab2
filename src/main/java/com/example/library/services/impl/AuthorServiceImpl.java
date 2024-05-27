package com.example.library.services.impl;

import com.example.library.dto.authors.AuthorDto;
import com.example.library.entities.Author;
import com.example.library.repositories.AuthorRepository;
import com.example.library.services.AuthorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        Author savedAuthor = authorRepository.save(author);
        log.info("Author created: {}", savedAuthor);
        return savedAuthor;
    }

    public List<Author> getAllAuthors() {
        log.info("Retrieving all authors");
        List<Author> authors = authorRepository.findAll();
        log.info("Retrieved {} authors", authors.size());
        return authors;
    }

    public Author getAuthorById(Long id) {
        log.info("Retrieving author with id: {}", id);
        Optional<Author> optionalAuthor = authorRepository.findById(id);
        if (optionalAuthor.isPresent()) {
            Author author = optionalAuthor.get();
            log.info("Author found: {}", author);
            return author;
        } else {
            log.info("Author with id {} not found", id);
            return null;
        }
    }

    public Author updateAuthor(Long id, AuthorDto dto) {
        log.info("Updating author with id: {}, new data: {}", id, dto);
        Optional<Author> optionalAuthor = authorRepository.findById(id);
        if (optionalAuthor.isPresent()) {
            Author existingAuthor = optionalAuthor.get();
            existingAuthor.setName(dto.getName());
            existingAuthor.setBio(dto.getBio());
            Author updatedAuthor = authorRepository.save(existingAuthor);
            log.info("Author updated: {}", updatedAuthor);
            return updatedAuthor;
        } else {
            log.info("Author with id {} not found", id);
            return null;
        }
    }

    public boolean deleteAuthor(Long id) {
        log.info("Deleting author with id: {}", id);
        Optional<Author> optionalAuthor = authorRepository.findById(id);
        if (optionalAuthor.isPresent()) {
            authorRepository.deleteById(id);
            log.info("Author with id {} deleted", id);
            return true;
        } else {
            log.info("Author with id {} not found", id);
            return false;
        }
    }
}