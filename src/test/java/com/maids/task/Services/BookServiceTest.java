package com.maids.task.Services;

import com.maids.task.Dto.BookDto;
import com.maids.task.Models.Book;
import com.maids.task.Repos.BookRepository;
import com.maids.task.Services.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveBook_Success() {
        BookDto bookDto = new BookDto();
        bookDto.setTitle("Test Title");
        bookDto.setAuthor("Test Author");
        bookDto.setPublicationDate(LocalDate.now());

        Book book = new Book();
        when(modelMapper.map(bookDto, Book.class)).thenReturn(book);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        ResponseEntity<?> response = bookService.saveBook(bookDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(book, response.getBody());
    }

    @Test
    public void testSaveBook_Failure() {
        BookDto bookDto = new BookDto(); // Missing fields

        ResponseEntity<?> response = bookService.saveBook(bookDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation failed: All fields must be non-null and Valid.", response.getBody());
    }

    @Test
    public void testGetBook_Success() {
        int id = 1;
        Book book = new Book();
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        ResponseEntity<?> response = bookService.getBook(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(book, response.getBody());
    }

    @Test
    public void testGetBook_NotFound() {
        int id = 1;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<?> response = bookService.getBook(id);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Book not found.", response.getBody());
    }

    // More tests for deleteBook, updateBook, etc.
}
