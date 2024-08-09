package com.maids.task.Services;


import com.maids.task.Dto.BorrowDto;
import com.maids.task.Models.Book;
import com.maids.task.Models.BorrowingRecord;
import com.maids.task.Models.Patron;
import com.maids.task.Repos.BookRepository;
import com.maids.task.Repos.BorrowRepository;
import com.maids.task.Repos.PatronRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BorrowService {

    private final BorrowRepository borrowRepository;
    private final BookRepository bookRepository;
    private final PatronRepository patronRepository;

    public ResponseEntity<?> saveBorrow(int bookId, int patronId , BorrowDto borrowDto) {
        Book book = bookRepository.findById(bookId)
                .orElse(null);

        Patron patron = patronRepository.findById(patronId)
                .orElse(null);
        if (book == null) {
            return ResponseEntity.badRequest().body("Book not found.");
        }
        if (patron == null) {
            return ResponseEntity.badRequest().body("Patron not found.");
        }
        LocalDate newBorrowingDate = borrowDto.getBorrowingDate();
        LocalDate newReturnDate = borrowDto.getReturnDate();

        if (newReturnDate != null && !newReturnDate.isAfter(newBorrowingDate)) {
            return ResponseEntity.badRequest().body("Return date must be after the borrowing date.");
        }
        List<BorrowingRecord> existingRecords = borrowRepository.findByBook(book);
        for (BorrowingRecord record : existingRecords) {
            LocalDate existingBorrowingDate = record.getBorrowingDate();
            LocalDate existingReturnDate = record.getReturnDate();

            if (newBorrowingDate.isEqual(existingBorrowingDate) ||
                    (existingReturnDate != null && !newBorrowingDate.isBefore(existingBorrowingDate) && !newBorrowingDate.isAfter(existingReturnDate))) {
                return ResponseEntity.badRequest().body("Book is already borrowed during the requested period.");
            }
            if (newReturnDate.isEqual(existingReturnDate) ||
                    (existingReturnDate != null && !newReturnDate.isBefore(existingBorrowingDate) && !newReturnDate.isAfter(existingReturnDate))) {
                return ResponseEntity.badRequest().body("Book is already borrowed during the requested period.");
            }
        }
        LocalDate borrowDate;
        if(borrowDto.getBorrowingDate() == null)
        {
            borrowDate = LocalDate.now();
        }
        else
        {
            borrowDate = borrowDto.getBorrowingDate();

        }
        BorrowingRecord borrowingRecord = BorrowingRecord.builder()
                .book(book)
                .patron(patron)
                .borrowingDate(borrowDate)
                .returnDate(borrowDto.getReturnDate())
                .build();

        return  ResponseEntity.status(HttpStatus.CREATED).body(borrowRepository.save(borrowingRecord));
    }

    public ResponseEntity<?> returnBook(int bookId, int patronId , BorrowDto borrowDto) {
        Book book = bookRepository.findById(bookId)
                .orElse(null);
        Patron patron = patronRepository.findById(patronId)
                .orElse(null);
        if (book == null) {
            return ResponseEntity.badRequest().body("Book not found.");
        }
        if (patron == null) {
            return ResponseEntity.badRequest().body("Patron not found.");
        }
        BorrowingRecord existingRecords = borrowRepository.findByBookAndPatronAndReturnDateIsNull(book, patron);
        if (existingRecords != null) {
            if(borrowDto.getReturnDate() == null)
            {
                existingRecords.setReturnDate(LocalDate.now());
            }
            else
            {
                existingRecords.setReturnDate(borrowDto.getReturnDate());
            }
            return ResponseEntity.ok(borrowRepository.save(existingRecords));
        }
        return ResponseEntity.badRequest().body("Borrow not found.");
    }

    public ResponseEntity<List<BorrowingRecord>> getAllBorrows() {
        return ResponseEntity.ok(borrowRepository.findAll());
    }


    public ResponseEntity<?> getBorrowBook(int id) {
        Book book = bookRepository.findById(id)
                .orElse(null);
        if (book != null) {
            List<BorrowingRecord> existingRecords = borrowRepository.findByBook(book);
            return ResponseEntity.ok(existingRecords);
        }
        else {
            return ResponseEntity.badRequest().body("Borrow not found.");
        }
    }

}
