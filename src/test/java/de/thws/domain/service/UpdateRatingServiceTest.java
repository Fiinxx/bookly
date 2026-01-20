package de.thws.domain.service;

import de.thws.domain.exception.EntityNotFoundException;
import de.thws.domain.model.Rating;
import de.thws.domain.port.out.UpdateRatingPort;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
public class UpdateRatingServiceTest {

    @Inject
    UpdateRatingService updateRatingService;

    @InjectMock
    UpdateRatingPort updateRatingPort;

    @Test
    void updateRating_should_return_updated_rating_when_update_is_successful() {
        Rating inputRating = new Rating();
        inputRating.setId(1L);
        inputRating.setRating(5);
        inputRating.setComment("Updated comment");

        Rating returnedRating = new Rating();
        returnedRating.setId(1L);
        returnedRating.setRating(5);
        returnedRating.setComment("Updated comment");
        // Assume database adds/preserves timestamps, etc.

        when(updateRatingPort.updateRating(inputRating)).thenReturn(returnedRating);

        Rating result = updateRatingService.updateRating(inputRating);

        assertEquals(returnedRating, result);
        verify(updateRatingPort).updateRating(inputRating);
    }

    @Test
    void updateRating_should_throw_EntityNotFoundException_when_rating_does_not_exist() {
        Rating inputRating = new Rating();
        inputRating.setId(99L);
        inputRating.setRating(3);

        when(updateRatingPort.updateRating(inputRating))
                .thenThrow(new EntityNotFoundException("Rating not found"));

        assertThrows(EntityNotFoundException.class, () ->
                updateRatingService.updateRating(inputRating)
        );
        verify(updateRatingPort).updateRating(inputRating);
    }
}