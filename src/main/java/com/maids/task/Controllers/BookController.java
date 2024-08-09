package com.maids.task.Controllers;


import com.maids.task.Dto.BookDto;
import com.maids.task.Models.Book;
import com.maids.task.Services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getBook(
            @PathVariable int id
    ) {
        return bookService.getBook(id);
    }

    @PostMapping
    public ResponseEntity<?> saveBook(
            @RequestBody BookDto book
    ) {
        return bookService.saveBook(book);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(
            @RequestBody BookDto book,
            @PathVariable int id) {
        return ResponseEntity.ok(bookService.updateBook(id, book));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(
            @PathVariable int id
    ) {
        return bookService.deleteBook(id);
    }



    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String errorMessage = "Invalid ID: " + ex.getValue() + " is not a valid integer.";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }


}

