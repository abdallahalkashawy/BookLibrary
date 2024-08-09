package com.maids.task.Services;

import com.maids.task.Dto.PatronDto;
import com.maids.task.Models.Patron;
import com.maids.task.Repos.PatronRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PatronServiceTest {

    @Mock
    private PatronRepository patronRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PatronService patronService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllPatrons() {
        // Arrange
        List<Patron> patrons = Arrays.asList(new Patron(), new Patron());
        when(patronRepository.findAll()).thenReturn(patrons);

        // Act
        ResponseEntity<List<Patron>> response = patronService.getAllPatrons();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testSavePatron() {
        // Arrange
        PatronDto patronDto = new PatronDto("John", "johndoe@example.com");
        Patron patron = new Patron();
        when(modelMapper.map(patronDto, Patron.class)).thenReturn(patron);
        when(patronRepository.save(patron)).thenReturn(patron);

        // Act
        Patron savedPatron = patronService.savePatron(patronDto);

        // Assert
        assertEquals(patron, savedPatron);
        verify(patronRepository, times(1)).save(patron);
    }

    @Test
    public void testGetPatron_Found() {
        // Arrange
        Patron patron = new Patron();
        when(patronRepository.findById(1)).thenReturn(Optional.of(patron));

        // Act
        ResponseEntity<?> response = patronService.getPatron(1);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(patron, response.getBody());
    }

    @Test
    public void testGetPatron_NotFound() {
        // Arrange
        when(patronRepository.findById(1)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = patronService.getPatron(1);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Patron not found.", response.getBody());
    }

    @Test
    public void testDeletePatron_Found() {
        // Arrange
        Patron patron = new Patron();
        when(patronRepository.findById(1)).thenReturn(Optional.of(patron));

        // Act
        ResponseEntity<?> response = patronService.deletePatron(1);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        verify(patronRepository, times(1)).delete(patron);
    }

    @Test
    public void testDeletePatron_NotFound() {
        // Arrange
        when(patronRepository.findById(1)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = patronService.deletePatron(1);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Patron not found.", response.getBody());
    }

    @Test
    public void testUpdatePatron_Found() {
        // Arrange
        PatronDto patronDto = new PatronDto("John", "johndoe@example.com");
        Patron patron = new Patron();
        when(patronRepository.findById(1)).thenReturn(Optional.of(patron));
        when(patronRepository.save(patron)).thenReturn(patron);

        // Act
        ResponseEntity<?> response = patronService.updatePatron(1, patronDto);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(patron, response.getBody());
        verify(modelMapper, times(1)).map(patronDto, patron);
    }

    @Test
    public void testUpdatePatron_NotFound() {
        // Arrange
        PatronDto patronDto = new PatronDto("John", "johndoe@example.com");
        when(patronRepository.findById(1)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = patronService.updatePatron(1, patronDto);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Patron not found.", response.getBody());
    }
}
