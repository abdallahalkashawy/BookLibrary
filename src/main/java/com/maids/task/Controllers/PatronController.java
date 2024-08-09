package com.maids.task.Controllers;

import com.maids.task.Dto.PatronDto;
import com.maids.task.Models.Patron;
import com.maids.task.Services.PatronService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@RestController
@RequestMapping("/api/patrons")
@RequiredArgsConstructor
public class PatronController {

    private final PatronService patronService;

    @GetMapping
    public ResponseEntity<List<Patron>> getAllPatrons() {
        return patronService.getAllPatrons();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPatron(
            @PathVariable int id
    ) {
        return patronService.getPatron(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePatron(
            @PathVariable int id
    ) {
        return patronService.deletePatron(id);
    }

    @PostMapping
    public ResponseEntity<?> savePatron(
            @RequestBody PatronDto patron
    ) {
        return ResponseEntity.ok(patronService.savePatron(patron));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePatron(
            @RequestBody PatronDto patron,
            @PathVariable int id
    ) {
        if (id <= 0) {
            return ResponseEntity.badRequest().body("Invalid ID: ID must be a positive integer.");
        }
        return patronService.updatePatron(id, patron);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String errorMessage = "Invalid ID: " + ex.getValue() + " is not a valid integer.";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }
}
