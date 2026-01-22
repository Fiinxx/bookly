package de.thws.adapter.out.persistance.repository;

import de.thws.domain.exception.DuplicateEntityException;
import de.thws.domain.exception.EntityNotFoundException;
import de.thws.domain.model.Rating;
import de.thws.domain.model.RatingFilter;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(2)
class RatingPersistenceAdapterTest {

    @Inject
    RatingPersistenceAdapter ratingPersistenceAdapter;

    @Test
    @TestTransaction
    void persistRating_should_save_and_set_id() {
        Rating rating = new Rating();
        rating.setRating(5);
        rating.setComment("New Test Rating");
        rating.setUserId(2L);
        rating.setBookId(2L);
        rating.setCreationTime(Instant.now());

        ratingPersistenceAdapter.persistRating(rating);

        assertNotNull(rating.getId());
        Optional<Rating> loaded = ratingPersistenceAdapter.readRatingById(rating.getId());
        assertTrue(loaded.isPresent());
        assertEquals("New Test Rating", loaded.get().getComment());
    }

    @Test
    @TestTransaction
    void persistRating_should_throw_DuplicateEntityException_when_user_already_rated_book() {
        Rating duplicateRating = new Rating();
        duplicateRating.setUserId(2L);
        duplicateRating.setBookId(1L);
        duplicateRating.setRating(3);

        assertThrows(DuplicateEntityException.class, () -> {
            ratingPersistenceAdapter.persistRating(duplicateRating);
        });
    }

    @Test
    void readAllRatings_filter_by_bookId() {
        RatingFilter filter = new RatingFilter();
        filter.setBookId(1L);

        List<Rating> result = ratingPersistenceAdapter.readAllRatings(filter, 1, 10);

        assertFalse(result.isEmpty());
        assertEquals(1, result.get(0).getBookId());
    }

    @Test
    void readAllRatings_filter_by_score() {
        RatingFilter filter = new RatingFilter();
        filter.setRating(5);

        List<Rating> result = ratingPersistenceAdapter.readAllRatings(filter, 1, 10);

        assertTrue(result.stream().allMatch(r -> r.getRating() == 5));
    }


    @Test
    @TestTransaction
    void updateRating_should_change_comment() {
        Rating rating = ratingPersistenceAdapter.readRatingById(1L).get();
        String newComment = "Updated via Test";
        rating.setComment(newComment);

        ratingPersistenceAdapter.updateRating(rating);

        Rating updated = ratingPersistenceAdapter.readRatingById(1L).get();
        assertEquals(newComment, updated.getComment());
    }

    @Test
    @TestTransaction
    void updateRating_should_throw_NotFound_on_invalid_id() {
        Rating rating = new Rating();
        rating.setId(9999L);

        assertThrows(EntityNotFoundException.class, () -> {
            ratingPersistenceAdapter.updateRating(rating);
        });
    }


    @Test
    @TestTransaction
    void deleteRatingById_should_remove_entry() {
        ratingPersistenceAdapter.deleteRatingById(1L);

        Optional<Rating> deleted = ratingPersistenceAdapter.readRatingById(1L);
        assertFalse(deleted.isPresent());
    }

    @Test
    void deleteRatingById_should_throw_NotFound_if_missing() {
        assertThrows(EntityNotFoundException.class, () -> {
            ratingPersistenceAdapter.deleteRatingById(8888L);
        });
    }
}