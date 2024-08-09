package com.maids.task.Services;

import com.maids.task.Dto.BorrowDto;
import com.maids.task.Models.Book;
import com.maids.task.Models.BorrowingRecord;
import com.maids.task.Models.Patron;
import com.maids.task.Repos.BookRepository;
import com.maids.task.Repos.BorrowRepository;
import com.maids.task.Repos.PatronRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BorrowServiceTest {

    @Mock
    private BorrowRepository borrowRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PatronRepository patronRepository;

    @InjectMocks
    private BorrowService borrowService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveBorrow_ShouldReturnBadRequestIfBookNotFound() {
        when(bookRepository.findById(anyInt())).thenReturn(Optional.empty());

        BorrowDto borrowDto = new BorrowDto(LocalDate.now(), LocalDate.now().plusDays(5));
        ResponseEntity<?> response = borrowService.saveBorrow(1, 1, borrowDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Book not found.", response.getBody());
    }

    @Test
    void saveBorrow_ShouldReturnBadRequestIfPatronNotFound() {
        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(new Book()));
        when(patronRepository.findById(anyInt())).thenReturn(Optional.empty());

        BorrowDto borrowDto = new BorrowDto(LocalDate.now(), LocalDate.now().plusDays(5));
        ResponseEntity<?> response = borrowService.saveBorrow(1, 1, borrowDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Patron not found.", response.getBody());
    }

    @Test
    void saveBorrow_ShouldReturnBadRequestIfReturnDateIsBeforeBorrowDate() {
        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(new Book()));
        when(patronRepository.findById(anyInt())).thenReturn(Optional.of(new Patron()));

        BorrowDto borrowDto = new BorrowDto(LocalDate.now(), LocalDate.now().minusDays(1));
        ResponseEntity<?> response = borrowService.saveBorrow(1, 1, borrowDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Return date must be after the borrowing date.", response.getBody());
    }

    @Test
    void saveBorrow_ShouldReturnBadRequestIfBookAlreadyBorrowed() {
        Book book = new Book();
        Patron patron = new Patron();

        BorrowingRecord existingRecord = new BorrowingRecord();
        existingRecord.setBorrowingDate(LocalDate.now());
        existingRecord.setReturnDate(LocalDate.now().plusDays(5));

        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(book));
        when(patronRepository.findById(anyInt())).thenReturn(Optional.of(patron));
        when(borrowRepository.findByBook(book)).thenReturn(List.of(existingRecord));

        BorrowDto borrowDto = new BorrowDto(LocalDate.now(), LocalDate.now().plusDays(3));
        ResponseEntity<?> response = borrowService.saveBorrow(1, 1, borrowDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Book is already borrowed during the requested period.", response.getBody());
    }

    @Test
    void saveBorrow_ShouldSaveBorrowingRecordIfValid() {
        Book book = new Book();
        Patron patron = new Patron();

        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(book));
        when(patronRepository.findById(anyInt())).thenReturn(Optional.of(patron));
        when(borrowRepository.findByBook(book)).thenReturn(new ArrayList<>());
        when(borrowRepository.save(any(BorrowingRecord.class))).thenReturn(new BorrowingRecord());

        BorrowDto borrowDto = new BorrowDto(LocalDate.now(), LocalDate.now().plusDays(3));
        ResponseEntity<?> response = borrowService.saveBorrow(1, 1, borrowDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(borrowRepository, times(1)).save(any(BorrowingRecord.class));
    }

    @Test
    void getAllBorrows_ShouldReturnAllBorrowingRecords() {
        List<BorrowingRecord> records = List.of(new BorrowingRecord(), new BorrowingRecord());
        when(borrowRepository.findAll()).thenReturn(records);

        ResponseEntity<List<BorrowingRecord>> response = borrowService.getAllBorrows();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(records, response.getBody());
    }
    @Test
    void getBorrowBook_ShouldReturnBorrowingRecordsForBook() {
        Book book = new Book();
        List<BorrowingRecord> records = List.of(new BorrowingRecord(), new BorrowingRecord());

        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(book));
        when(borrowRepository.findByBook(book)).thenReturn(records);

        ResponseEntity<?> response = borrowService.getBorrowBook(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(records, response.getBody());
    }

    @Test
    void getBorrowBook_ShouldReturnBadRequestIfBookNotFound() {
        when(bookRepository.findById(anyInt())).thenReturn(Optional.empty());

        ResponseEntity<?> response = borrowService.getBorrowBook(1);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Borrow not found.", response.getBody());
    }
}
