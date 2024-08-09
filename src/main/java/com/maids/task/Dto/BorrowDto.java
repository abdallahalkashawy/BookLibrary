package com.maids.task.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowDto {

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate borrowingDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate returnDate;
}
