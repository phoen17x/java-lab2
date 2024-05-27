package com.example.library.services;

import com.example.library.dto.books.BookDto;
import com.example.library.entities.Author;
import com.example.library.entities.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Book createBook(BookDto dto);
    List<Book> getAllBooks();
    Book getBookById(Long id);
    Book updateBook(Long id, BookDto dto);
    boolean deleteBook(Long id);
}
