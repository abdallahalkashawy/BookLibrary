package com.maids.task.Controllers;

import com.maids.task.Dto.BorrowDto;
import com.maids.task.Services.BorrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Date;

@RestController
@RequestMapping("/api/borrow")
@RequiredArgsConstructor
public class BorrowController {

    private final BorrowService borrowService;

    @PostMapping("/{bookId}/patron/{patronId}")
    public ResponseEntity<?> borrowBook(
            @PathVariable int bookId,
            @PathVariable int patronId,
            @RequestBody BorrowDto borrowDto
    ) {
        return borrowService.saveBorrow(bookId, patronId ,borrowDto );
    }

    @PutMapping("/{bookId}/patron/{patronId}")
    public ResponseEntity<?> returnBook(
            @PathVariable int bookId,
            @PathVariable int patronId,
            @RequestBody BorrowDto borrowDto
    ) {
        return borrowService.returnBook(bookId, patronId , borrowDto);
    }

    @GetMapping
    public ResponseEntity<?> getAllBorrows() {
        return borrowService.getAllBorrows();
    }

    @GetMapping("{id}/borrow")
    public ResponseEntity<?> getBorrowedBooks(
            @PathVariable int id
    ) {
        return borrowService.getBorrowBook(id);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<?> handleDateTimeParseException(DateTimeParseException ex, WebRequest request) {
        String errorMessage = "Invalid date format: " + ex.getParsedString();
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
