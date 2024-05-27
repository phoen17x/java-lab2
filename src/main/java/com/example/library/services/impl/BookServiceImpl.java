package com.example.library.services.impl;

import com.example.library.dto.books.BookDto;
import com.example.library.entities.Author;
import com.example.library.entities.Book;
import com.example.library.repositories.AuthorRepository;
import com.example.library.repositories.BookRepository;
import com.example.library.services.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public Book createBook(BookDto dto) {
        log.info("Creating book: {}", dto);
        Author author = authorRepository
                .findById(dto.getAuthorId())
                .orElseThrow(() -> {
                    log.error("Author not found with id: {}", dto.getAuthorId());
                    return new RuntimeException("Author not found");
                });

        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setAuthor(author);
        Book savedBook = bookRepository.save(book);
        log.info("Book created: {}", savedBook);
        return savedBook;
    }

    public List<Book> getAllBooks() {
        log.info("Retrieving all books");
        List<Book> books = bookRepository.findAll();
        log.info("Retrieved {} books", books.size());
        return books;
    }

    public Book getBookById(Long id) {
        log.info("Retrieving book with id: {}", id);
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            log.info("Book found: {}", book);
            return book;
        } else {
            log.info("Book with id {} not found", id);
            return null;
        }
    }

    public Book updateBook(Long id, BookDto dto) {
        log.info("Updating book with id: {}, new data: {}", id, dto);
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            Author author = authorRepository
                    .findById(dto.getAuthorId())
                    .orElseThrow(() -> {
                        log.error("Author not found with id: {}", dto.getAuthorId());
                        return new RuntimeException("Author not found");
                    });

            Book existingBook = optionalBook.get();
            existingBook.setTitle(dto.getTitle());
            existingBook.setAuthor(author);
            Book updatedBook = bookRepository.save(existingBook);
            log.info("Book updated: {}", updatedBook);
            return updatedBook;
        } else {
            log.info("Book with id {} not found", id);
            return null;
        }
    }

    public boolean deleteBook(Long id) {
        log.info("Deleting book with id: {}", id);
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            bookRepository.deleteById(id);
            log.info("Book with id {} deleted", id);
            return true;
        } else {
            log.info("Book with id {} not found", id);
            return false;
        }
    }
}