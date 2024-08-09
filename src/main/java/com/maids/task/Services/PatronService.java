package com.maids.task.Services;

import com.maids.task.Dto.PatronDto;
import com.maids.task.Models.Book;
import com.maids.task.Models.Patron;
import com.maids.task.Repos.PatronRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatronService {

    private final PatronRepository patronRepository;
    private final ModelMapper modelMapper;

    public ResponseEntity<List<Patron>> getAllPatrons() {
        return ResponseEntity.ok(patronRepository.findAll());
    }

    public Patron savePatron(PatronDto patron) {
        Patron newPatron = modelMapper.map(patron, Patron.class);
        return patronRepository.save(newPatron);
    }

    public ResponseEntity<?> getPatron(int id) {
        Patron existingPatron = patronRepository.findById(id).orElse(null);
        if (existingPatron != null) {
            return ResponseEntity.ok(patronRepository.findById(id).orElse(null));
        }
        else {
            return ResponseEntity.badRequest().body("Patron not found.");
        }
    }


    public ResponseEntity<?> deletePatron(int id) {
        Patron existingPatron = patronRepository.findById(id).orElse(null);
        if (existingPatron != null) {
            patronRepository.delete(existingPatron);
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.badRequest().body("Patron not found.");
        }
    }

    public ResponseEntity<?> updatePatron(int id, PatronDto patronDto) {
        Patron existingPatron = patronRepository.findById(id).orElse(null);
        if (existingPatron != null) {
            modelMapper.map(patronDto, existingPatron);
            return ResponseEntity.ok(patronRepository.save(existingPatron));
        }
        else {
            return ResponseEntity.badRequest().body("Patron not found.");
        }
    }




}
