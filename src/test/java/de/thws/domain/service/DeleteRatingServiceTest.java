package de.thws.domain.service;

import de.thws.domain.exception.EntityNotFoundException;
import de.thws.domain.port.out.DeleteRatingPort;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class DeleteRatingServiceTest {

    @Inject
    DeleteRatingService deleteRatingService;

    @InjectMock
    DeleteRatingPort deleteRatingPort;

    @Test
    void deleteRatingById_should_delegate_to_port_on_success() {
        long ratingId = 1L;
        // Konfiguration für den Erfolgsfall
        Mockito.doNothing().when(deleteRatingPort).deleteRatingById(ratingId);
        assertDoesNotThrow(() -> deleteRatingService.deleteRatingById(ratingId));
        Mockito.verify(deleteRatingPort, Mockito.times(1)).deleteRatingById(ratingId);
    }

    @Test
    void deleteRatingById_should_propagate_EntityNotFoundException_when_rating_missing() {
        long nonExistingId = 999L;
        //Port wirft Exception, wenn Rating nicht existiert
        Mockito.doThrow(new EntityNotFoundException("Rating not found"))
                .when(deleteRatingPort).deleteRatingById(nonExistingId);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            deleteRatingService.deleteRatingById(nonExistingId);
        });

        assertEquals("Rating not found", exception.getMessage());
        Mockito.verify(deleteRatingPort).deleteRatingById(nonExistingId);
    }
}