package com.example.library.controllers;

import com.example.library.dto.authors.AuthorDto;
import com.example.library.entities.Author;
import com.example.library.services.AuthorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Author> createAuthor(@Validated @RequestBody AuthorDto author) {
        log.info("Creating author: {}", author);

        Author savedAuthor = authorService.createAuthor(author);

        log.info("Author created: {}", savedAuthor);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAuthor);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors() {
        log.info("Retrieving all authors");

        List<Author> authors = authorService.getAllAuthors();

        log.info("Retrieved {} authors", authors.size());
        return ResponseEntity.ok(authors);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable Long id) {
        log.info("Retrieving author with id: {}", id);

        Author author = authorService.getAuthorById(id);

        log.info("Author found: {}", author);
        return ResponseEntity.ok(author);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Author> updateAuthor(@PathVariable Long id, @Validated @RequestBody AuthorDto author) {
        log.info("Updating author with id: {}, new data: {}", id, author);

        Author updatedAuthor = authorService.updateAuthor(id, author);

        log.info("Author updated: {}", updatedAuthor);
        return ResponseEntity.ok(updatedAuthor);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        log.info("Deleting author with id: {}", id);

        authorService.deleteAuthor(id);

        log.info("Author with id {} deleted", id);
        return ResponseEntity.noContent().build();
    }
}