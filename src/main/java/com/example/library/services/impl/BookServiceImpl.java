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

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public Book createBook(BookDto dto) {
        log.info("Creating book: {}", dto);

        Author author = authorRepository.findById(dto.getAuthorId()).orElseThrow();
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setAuthor(author);
        bookRepository.save(book);

        log.info("Book created: {}", book);
        return book;
    }

    public List<Book> getAllBooks() {
        log.info("Retrieving all books");

        List<Book> books = bookRepository.findAll();

        log.info("Retrieved {} books", books.size());
        return books;
    }

    public Book getBookById(Long id) {
        log.info("Retrieving book with id: {}", id);

        Book book = bookRepository.findById(id).orElseThrow();

        log.info("Book found: {}", book);
        return book;
    }

    public Book updateBook(Long id, BookDto dto) {
        log.info("Updating book with id: {}, new data: {}", id, dto);

        Author author = authorRepository.findById(dto.getAuthorId()).orElseThrow();
        Book book = bookRepository.findById(id).orElseThrow();
        book.setTitle(dto.getTitle());
        book.setAuthor(author);
        bookRepository.update(book);

        log.info("Book updated: {}", book);
        return book;
    }

    public void deleteBook(Long id) {
        log.info("Deleting book with id: {}", id);

        bookRepository.deleteById(id);

        log.info("Book with id {} deleted", id);
    }
}