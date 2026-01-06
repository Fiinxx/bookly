package de.thws.adapter.out.persistance.repository;

import de.thws.domain.model.Rating;
import de.thws.domain.port.out.DeleteRatingPort;
import de.thws.domain.port.out.PersistRatingPort;
import de.thws.domain.port.out.ReadRatingPort;
import de.thws.domain.port.out.UpdateRatingPort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class RatingPersistenceAdapter implements DeleteRatingPort, UpdateRatingPort, PersistRatingPort, ReadRatingPort {

    @Override
    public void deleteRating(Rating rating) {

    }

    @Override
    public void persistRating(Rating rating) {

    }

    @Override
    public Optional<Rating> readRatingById(Long id) {
        return null;
    }

    @Override
    public List<Rating> readAllRatings() {
        return List.of();
    }

    @Override
    public void updateRating(int rating) {

    }
}
