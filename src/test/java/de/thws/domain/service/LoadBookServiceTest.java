package de.thws.domain.service;

import de.thws.domain.exception.EntityNotFoundException;
import de.thws.domain.model.Book;
import de.thws.domain.port.out.ReadBookPort;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;

@QuarkusTest
public class LoadBookServiceTest {

    @Inject
    LoadBookService loadBookService;

    @InjectMock
    ReadBookPort readBookPort;

    @Test
    void loadBookById_should_return_book_when_exists() {
        Book mockBook = new Book();
        mockBook.setId(1L);
        mockBook.setTitle("Test Book");

        Mockito.when(readBookPort.readBookById(1L)).thenReturn(Optional.of(mockBook));

        Book result = loadBookService.loadBookbyId(1L);

        assertNotNull(result);
        assertEquals("Test Book", result.getTitle());
    }

    @Test
    void loadBookById_should_throw_exception_when_not_found() {
        Mockito.when(readBookPort.readBookById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            loadBookService.loadBookbyId(99L);
        });
    }
}