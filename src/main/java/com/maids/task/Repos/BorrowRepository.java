package com.maids.task.Repos;

import com.maids.task.Models.Book;
import com.maids.task.Models.BorrowingRecord;
import com.maids.task.Models.Patron;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowRepository extends JpaRepository<BorrowingRecord, Integer> {

    List<BorrowingRecord> findByBook(Book book);
    BorrowingRecord findByBookAndPatronAndReturnDateIsNull(Book book, Patron patron);
}
