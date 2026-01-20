package de.thws.domain.service;

import de.thws.domain.exception.BusinessRuleViolationException;
import de.thws.domain.exception.DuplicateEntityException;
import de.thws.domain.model.Rating;
import de.thws.domain.port.out.PersistRatingPort;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@QuarkusTest
class CreateRatingServiceTest {

    @Inject
    CreateRatingService createRatingService;

    @InjectMock
    PersistRatingPort persistRatingPort;

    private Rating testRating;

    @BeforeEach
    void setUp() {
        testRating = new Rating();
        testRating.setRating(5);
        testRating.setComment("Klasse Buch!");
        testRating.setUserId(1L);
        testRating.setBookId(1L);
    }

    @Test
    void createRating_should_set_creationTime_before_persisting() {
        //ArgumentCaptor zum Objekt untersuchen, das an den Port übergeben wird
        ArgumentCaptor<Rating> ratingCaptor = ArgumentCaptor.forClass(Rating.class);

        createRatingService.createRating(testRating);

        // Verifizieren, dass persistRating aufgerufen wurde
        verify(persistRatingPort).persistRating(ratingCaptor.capture());

        Rating savedRating = ratingCaptor.getValue();

        // Prüfen, ob creationTime gesetzt wurde
        assertNotNull(savedRating.getCreationTime(), "Die CreationTime sollte vom Service gesetzt werden.");

        // Prüfen, ob Zeitstempel aktuell ist
        assertTrue(savedRating.getCreationTime().isBefore(Instant.now().plusSeconds(1)));
        assertTrue(savedRating.getCreationTime().isAfter(Instant.now().minusSeconds(5)));
    }

    @Test
    void createRating_should_delegate_to_persistRatingPort() {
        createRatingService.createRating(testRating);

        // Sicherstellen, dass Methode im Port mit unserem Rating aufgerufen wird
        verify(persistRatingPort).persistRating(testRating);
    }

    @Test
    void createRating_with_invalid_score_should_throw_BusinessRuleViolationException() {
        Rating invalidRating = new Rating();
        invalidRating.setUserId(1L);
        invalidRating.setBookId(1L);

        assertThrows(BusinessRuleViolationException.class, () -> {
            invalidRating.setRating(6);
        }, "Ein Rating von 6 sollte eine BusinessRuleViolationException auslösen.");
    }

    @Test
    void createRating_should_propagate_DuplicateEntityException() {
        Rating duplicateRating = new Rating();
        duplicateRating.setRating(3);
        duplicateRating.setUserId(1L);
        duplicateRating.setBookId(1L);

        Mockito.doThrow(new DuplicateEntityException("User has already rated this book."))
                .when(persistRatingPort).persistRating(any(Rating.class));

        assertThrows(DuplicateEntityException.class, () -> {
            createRatingService.createRating(duplicateRating);
        }, "Der Service muss die DuplicateEntityException nach oben durchreichen.");
    }
}