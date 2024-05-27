package com.example.library.services;

import com.example.library.dto.books.BookDto;
import com.example.library.entities.Author;
import com.example.library.entities.Book;
import com.example.library.repositories.AuthorRepository;
import com.example.library.repositories.BookRepository;
import com.example.library.services.impl.BookServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    public void givenBookDto_whenCreateBook_thenReturnCreatedBook() {
        // arrange
        BookDto dto = new BookDto("Book Title", 1L);
        Author author = new Author(1L, "John Doe", "Software Engineer");
        Book book = new Book(1L, "Book Title", author);

        when(authorRepository.findById(dto.getAuthorId())).thenReturn(Optional.of(author));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        // act
        Book createdBook = bookService.createBook(dto);

        // assert
        assertNotNull(createdBook);
        assertEquals(book.getTitle(), createdBook.getTitle());
        assertEquals(book.getAuthor().getId(), createdBook.getAuthor().getId());
        verify(authorRepository).findById(dto.getAuthorId());
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    public void givenNonExistingAuthorId_whenCreateBook_thenThrowException() {
        // arrange
        BookDto dto = new BookDto("Book Title", 1L);

        when(authorRepository.findById(dto.getAuthorId())).thenReturn(Optional.empty());

        // act, assert
        assertThrows(RuntimeException.class, () -> bookService.createBook(dto));
        verify(authorRepository).findById(dto.getAuthorId());
    }

    @Test
    public void givenNoBooks_whenGetAllBooks_thenReturnEmptyList() {
        // arrange
        when(bookRepository.findAll()).thenReturn(List.of());

        // act
        List<Book> books = bookService.getAllBooks();

        // assert
        assertNotNull(books);
        assertTrue(books.isEmpty());
        verify(bookRepository).findAll();
    }

    @Test
    public void givenSomeBooks_whenGetAllBooks_thenReturnBookList() {
        // arrange
        Author author = new Author(1L, "John Doe", "Software Engineer");
        Book book1 = new Book(1L, "Book 1", author);
        Book book2 = new Book(2L, "Book 2", author);
        List<Book> books = Arrays.asList(book1, book2);

        when(bookRepository.findAll()).thenReturn(books);

        // act
        List<Book> retrievedBooks = bookService.getAllBooks();

        // assert
        assertNotNull(retrievedBooks);
        assertEquals(books.size(), retrievedBooks.size());
        verify(bookRepository).findAll();
    }

    @Test
    public void givenExistingBookId_whenGetBookById_thenReturnBook() {
        // arrange
        Long bookId = 1L;
        Author author = new Author(1L, "John Doe", "Software Engineer");
        Book book = new Book(bookId, "Book Title", author);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        // act
        Book retrievedBook = bookService.getBookById(bookId);

        // assert
        assertNotNull(retrievedBook);
        assertEquals(book.getId(), retrievedBook.getId());
        assertEquals(book.getTitle(), retrievedBook.getTitle());
        assertEquals(book.getAuthor().getId(), retrievedBook.getAuthor().getId());
        verify(bookRepository).findById(bookId);
    }

    @Test
    public void givenNonExistingBookId_whenGetBookById_thenReturnNull() {
        // arrange
        Long bookId = 1L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // act
        Book retrievedBook = bookService.getBookById(bookId);

        // assert
        assertNull(retrievedBook);
        verify(bookRepository).findById(bookId);
    }

    @Test
    public void givenExistingBookIdAndDto_whenUpdateBook_thenReturnUpdatedBook() {
        // arrange
        Long bookId = 1L;
        BookDto dto = new BookDto("Updated Book Title", 1L);
        Author author = new Author(1L, "John Doe", "Software Engineer");
        Book existingBook = new Book(bookId, "Book Title", author);
        Book updatedBook = new Book(bookId, "Updated Book Title", author);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(authorRepository.findById(dto.getAuthorId())).thenReturn(Optional.of(author));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);

        // act
        Book result = bookService.updateBook(bookId, dto);

        // assert
        assertNotNull(result);
        assertEquals(updatedBook.getTitle(), result.getTitle());
        assertEquals(updatedBook.getAuthor().getId(), result.getAuthor().getId());
        verify(bookRepository).findById(bookId);
        verify(authorRepository).findById(dto.getAuthorId());
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    public void givenNonExistingBookId_whenUpdateBook_thenReturnNull() {
        // arrange
        Long bookId = 1L;
        BookDto dto = new BookDto("Updated Book Title", 1L);

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // act
        Book result = bookService.updateBook(bookId, dto);

        // assert
        assertNull(result);
        verify(bookRepository).findById(bookId);
    }

    @Test
    public void givenNonExistingAuthorId_whenUpdateBook_thenThrowException() {
        // arrange
        Long bookId = 1L;
        BookDto dto = new BookDto("Updated Book Title", 1L);
        Author author = new Author(1L, "John Doe", "Software Engineer");
        Book existingBook = new Book(bookId, "Book Title", author);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(authorRepository.findById(dto.getAuthorId())).thenReturn(Optional.empty());

        // act, assert
        assertThrows(RuntimeException.class, () -> bookService.updateBook(bookId, dto));
        verify(bookRepository).findById(bookId);
        verify(authorRepository).findById(dto.getAuthorId());
    }

    @Test
    public void givenExistingBookId_whenDeleteBook_thenReturnTrue() {
        // arrange
        Long bookId = 1L;
        Author author = new Author(1L, "John Doe", "Software Engineer");
        Book book = new Book(bookId, "Book Title", author);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        // act
        boolean isDeleted = bookService.deleteBook(bookId);

        // assert
        assertTrue(isDeleted);
        verify(bookRepository).findById(bookId);
        verify(bookRepository).deleteById(bookId);
    }

    @Test
    public void givenNonExistingBookId_whenDeleteBook_thenReturnFalse() {
        // arrange
        Long bookId = 1L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // act
        boolean isDeleted = bookService.deleteBook(bookId);

        // assert
        assertFalse(isDeleted);
        verify(bookRepository).findById(bookId);
    }
}
