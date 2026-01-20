package de.thws.domain.service;

import de.thws.domain.exception.EntityNotFoundException;
import de.thws.domain.port.out.DeleteBookPort;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class DeleteBookServiceTest {

    @Inject
    DeleteBookService deleteBookService;

    @InjectMock
    DeleteBookPort deleteBookPort;

    @Test
    void deleteBookById_should_delegate_to_port_on_success() {
        long bookId = 1L;

        Mockito.doNothing().when(deleteBookPort).deleteBookById(bookId);

        // Sicherstellen, dass bei einer existierenden ID keine Exception geworfen wird
        assertDoesNotThrow(() -> deleteBookService.deleteBookById(bookId));

        // Verifizieren, dass Port tatsächlich aufgerufen wurde
        Mockito.verify(deleteBookPort, Mockito.times(1)).deleteBookById(bookId);
    }

    @Test
    void deleteBookById_should_propagate_EntityNotFoundException_when_book_missing() {
        long nonExistingId = 999L;
        // Simulation, Port wirft Exception, wenn das Buch fehlt
        Mockito.doThrow(new EntityNotFoundException("Book not found"))
                .when(deleteBookPort).deleteBookById(nonExistingId);

        // Der Service muss die Exception eins-zu-eins nach oben durchreichen
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            deleteBookService.deleteBookById(nonExistingId);
        });

        assertEquals("Book not found", exception.getMessage());
        Mockito.verify(deleteBookPort).deleteBookById(nonExistingId);
    }
}