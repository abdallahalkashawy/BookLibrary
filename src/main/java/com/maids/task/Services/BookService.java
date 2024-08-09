package com.maids.task.Services;

import com.maids.task.Dto.BookDto;
import com.maids.task.Models.Book;
import com.maids.task.Repos.BookRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public ResponseEntity<?> saveBook(BookDto bookDto) {
        if (isBookDtoValid(bookDto)) {
            Book newBook = modelMapper.map(bookDto, Book.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(bookRepository.save(newBook)); // Return the book with a 201 Created status
        } else {
            String errorMessage = "Validation failed: All fields must be non-null and Valid.";
            return ResponseEntity.badRequest().body(errorMessage); // Return the error message with a 400 Bad Request status
        }
    }

    private boolean isBookDtoValid(BookDto bookDto) {
        return bookDto.getTitle() != null &&
                bookDto.getAuthor() != null &&
                bookDto.getPublicationDate() != null ;
    }

    public ResponseEntity<?> getBook(int id) {
        Book existingBook = bookRepository.findById(id).orElse(null);
        if (existingBook != null) {
            return ResponseEntity.ok(bookRepository.findById(id).orElse(null));
        }
        else {
            return ResponseEntity.badRequest().body("Book not found.");
        }
    }

    public ResponseEntity<?> deleteBook(int id) {
        Book existingBook = bookRepository.findById(id).orElse(null);
        if (existingBook != null) {
            bookRepository.delete(existingBook);
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.badRequest().body("Book not found.");
        }
    }

    public Book updateBook(int id, BookDto bookDto) {
        Book existingBook = bookRepository.findById(id).orElse(null);
        if (existingBook != null) {
            modelMapper.map(bookDto, existingBook);

            return bookRepository.save(existingBook);
        }
        return null;
    }


}
