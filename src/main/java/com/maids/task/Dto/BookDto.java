package com.maids.task.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    private String title;
    private String author;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate publicationDate;
    private String ISBN;
}
