package com.maids.task.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Patron {
    @Id
    @GeneratedValue
    private Integer id;


    @Column
    private String name;

    @Column
    private String contactInfo;

    @OneToMany(mappedBy = "patron")
//    @JsonManagedReference
    @JsonIgnore
    private List<BorrowingRecord> borrowingRecords;

    @Override
    public String toString() {
        return "Patron{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", contactInfo='" + contactInfo + '\'' +
                '}';
    }

}
