package com.example.library.controllers;

import com.example.library.dto.books.BookDto;
import com.example.library.entities.Book;
import com.example.library.services.BookService;
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
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Book> createBook(@Validated @RequestBody BookDto book) {
        log.info("Creating book: {}", book);

        Book savedBook = bookService.createBook(book);

        log.info("Book created: {}", savedBook);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        log.info("Retrieving all books");

        List<Book> books = bookService.getAllBooks();

        log.info("Retrieved {} books", books.size());
        return ResponseEntity.ok(books);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        log.info("Retrieving book with id: {}", id);

        Book book = bookService.getBookById(id);

        log.info("Book found: {}", book);
        return ResponseEntity.ok(book);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @Validated @RequestBody BookDto book) {
        log.info("Updating book with id: {}, new data: {}", id, book);

        Book updatedBook = bookService.updateBook(id, book);

        log.info("Book updated: {}", updatedBook);
        return ResponseEntity.ok(updatedBook);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        log.info("Deleting book with id: {}", id);

        bookService.deleteBook(id);

        log.info("Book with id {} deleted", id);
        return ResponseEntity.noContent().build();
    }
}