package com.example.library.services;

import com.example.library.dto.authors.AuthorDto;
import com.example.library.entities.Author;
import com.example.library.repositories.AuthorRepository;
import com.example.library.services.impl.AuthorServiceImpl;
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
public class AuthorServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    @Test
    public void givenAuthorDto_whenCreateAuthor_thenReturnCreatedAuthor() {
        // arrange
        AuthorDto dto = new AuthorDto("John Doe", "Software Engineer");
        Author author = new Author();
        author.setName(dto.getName());
        author.setBio(dto.getBio());

        when(authorRepository.save(any(Author.class))).thenReturn(author);

        // act
        Author createdAuthor = authorService.createAuthor(dto);

        // assert
        assertNotNull(createdAuthor);
        assertEquals(author.getName(), createdAuthor.getName());
        assertEquals(author.getBio(), createdAuthor.getBio());
        verify(authorRepository).save(any(Author.class));
    }

    @Test
    public void givenNoAuthors_whenGetAllAuthors_thenReturnEmptyList() {
        // arrange
        when(authorRepository.findAll()).thenReturn(List.of());

        // act
        List<Author> authors = authorService.getAllAuthors();

        // assert
        assertNotNull(authors);
        assertTrue(authors.isEmpty());
        verify(authorRepository).findAll();
    }

    @Test
    public void givenSomeAuthors_whenGetAllAuthors_thenReturnAuthorList() {
        // arrange
        Author author1 = new Author(1L, "John Doe", "Software Engineer");
        Author author2 = new Author(2L, "Jane Smith", "Technical Writer");
        List<Author> authors = Arrays.asList(author1, author2);

        when(authorRepository.findAll()).thenReturn(authors);

        // act
        List<Author> retrievedAuthors = authorService.getAllAuthors();

        // assert
        assertNotNull(retrievedAuthors);
        assertEquals(authors.size(), retrievedAuthors.size());
        verify(authorRepository).findAll();
    }

    @Test
    public void givenExistingAuthorId_whenGetAuthorById_thenReturnAuthor() {
        // arrange
        Long authorId = 1L;
        Author author = new Author(authorId, "John Doe", "Software Engineer");

        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));

        // act
        Author retrievedAuthor = authorService.getAuthorById(authorId);

        // assert
        assertNotNull(retrievedAuthor);
        assertEquals(author.getId(), retrievedAuthor.getId());
        assertEquals(author.getName(), retrievedAuthor.getName());
        assertEquals(author.getBio(), retrievedAuthor.getBio());
        verify(authorRepository).findById(authorId);
    }

    @Test
    public void givenNonExistingAuthorId_whenGetAuthorById_thenReturnNull() {
        // arrange
        Long authorId = 1L;

        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        // act
        Author retrievedAuthor = authorService.getAuthorById(authorId);

        // assert
        assertNull(retrievedAuthor);
        verify(authorRepository).findById(authorId);
    }

    @Test
    public void givenExistingAuthorIdAndDto_whenUpdateAuthor_thenReturnUpdatedAuthor() {
        // arrange
        Long authorId = 1L;
        Author existingAuthor = new Author(authorId, "John Doe", "Software Engineer");
        AuthorDto dto = new AuthorDto("John Doe Updated", "Senior Software Engineer");

        when(authorRepository.findById(authorId)).thenReturn(Optional.of(existingAuthor));
        when(authorRepository.save(any(Author.class))).thenReturn(existingAuthor);

        // act
        Author updatedAuthor = authorService.updateAuthor(authorId, dto);

        // assert
        assertNotNull(updatedAuthor);
        assertEquals(dto.getName(), updatedAuthor.getName());
        assertEquals(dto.getBio(), updatedAuthor.getBio());
        verify(authorRepository).findById(authorId);
        verify(authorRepository).save(any(Author.class));
    }

    @Test
    public void givenNonExistingAuthorId_whenUpdateAuthor_thenReturnNull() {
        // arrange
        Long authorId = 1L;
        AuthorDto dto = new AuthorDto("John Doe Updated", "Senior Software Engineer");

        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        // act
        Author updatedAuthor = authorService.updateAuthor(authorId, dto);

        // assert
        assertNull(updatedAuthor);
        verify(authorRepository).findById(authorId);
    }

    @Test
    public void givenExistingAuthorId_whenDeleteAuthor_thenReturnTrue() {
        // arrange
        Long authorId = 1L;
        Author author = new Author(authorId, "John Doe", "Software Engineer");

        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));

        // act
        boolean isDeleted = authorService.deleteAuthor(authorId);

        // assert
        assertTrue(isDeleted);
        verify(authorRepository).findById(authorId);
        verify(authorRepository).deleteById(authorId);
    }

    @Test
    public void givenNonExistingAuthorId_whenDeleteAuthor_thenReturnFalse() {
        // arrange
        Long authorId = 1L;

        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        // act
        boolean isDeleted = authorService.deleteAuthor(authorId);

        // assert
        assertFalse(isDeleted);
        verify(authorRepository).findById(authorId);
    }
}