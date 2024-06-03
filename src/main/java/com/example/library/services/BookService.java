package com.example.library.services;

import com.example.library.dto.books.BookDto;
import com.example.library.entities.Book;

import java.util.List;

public interface BookService {

    Book createBook(BookDto dto);
    List<Book> getAllBooks();
    Book getBookById(Long id);
    Book updateBook(Long id, BookDto dto);
    void deleteBook(Long id);
}
