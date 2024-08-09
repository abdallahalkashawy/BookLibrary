package com.maids.task.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BorrowingRecord {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
//    @JsonBackReference
    private Book book;

    @ManyToOne
    @JoinColumn(name = "patron_id", nullable = false)
//    @JsonBackReference
    private Patron patron;


    @Column(name = "borrowing_date", nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate borrowingDate;

    @Column(name = "return_date")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate returnDate;

    @Override
    public String toString() {
        return "BorrowingRecord{" +
                "id=" + id +
                ", borrowingDate=" + borrowingDate +
                ", returnDate=" + returnDate +
                '}';
    }
}
