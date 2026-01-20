package de.thws.domain.service;

import de.thws.domain.exception.EntityNotFoundException;
import de.thws.domain.model.Rating;
import de.thws.domain.model.RatingFilter;
import de.thws.domain.port.out.ReadRatingPort;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@QuarkusTest
public class LoadRatingServiceTest {

    @Inject
    LoadRatingService loadRatingService;

    @InjectMock
    ReadRatingPort readRatingPort;

    @Test
    void loadAllRatings_should_return_list_of_ratings() {
        // Arrange
        Rating rating = new Rating();
        rating.setId(1L);
        rating.setRating(5);
        List<Rating> expectedRatings = List.of(rating);
        RatingFilter filter = new RatingFilter();

        when(readRatingPort.readAllRatings(filter, 1, 10)).thenReturn(expectedRatings);

        // Act
        List<Rating> result = loadRatingService.loadAllRatings(filter, 1, 10);

        // Assert
        assertEquals(expectedRatings.size(), result.size());
        assertEquals(expectedRatings.get(0), result.get(0));
        verify(readRatingPort).readAllRatings(filter, 1, 10);
    }

    @Test
    void loadAllRatings_should_return_empty_list_when_none_found() {
        // Arrange
        RatingFilter filter = new RatingFilter();
        when(readRatingPort.readAllRatings(any(RatingFilter.class), anyInt(), anyInt()))
                .thenReturn(Collections.emptyList());

        // Act
        List<Rating> result = loadRatingService.loadAllRatings(filter, 1, 10);

        // Assert
        assertTrue(result.isEmpty());
        verify(readRatingPort).readAllRatings(filter, 1, 10);
    }

    @Test
    void loadRatingById_should_return_rating_when_found() {
        Rating rating = new Rating();
        rating.setId(1L);

        when(readRatingPort.readRatingById(1L)).thenReturn(Optional.of(rating));

        Rating result = loadRatingService.loadRatingById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(readRatingPort).readRatingById(1L);
    }

    @Test
    void loadRatingById_should_throw_exception_when_not_found() {
        when(readRatingPort.readRatingById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                loadRatingService.loadRatingById(1L)
        );

        assertEquals("Rating with id 1 not found", exception.getMessage());
        verify(readRatingPort).readRatingById(1L);
    }
}