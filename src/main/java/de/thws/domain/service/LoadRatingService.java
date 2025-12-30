package de.thws.domain.service;

import de.thws.domain.model.Rating;
import de.thws.domain.port.in.LoadRatingUseCase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class LoadRatingService implements LoadRatingUseCase {

    @Override
    public List<Rating> loadAllRatings() {
        return List.of();
    }

    @Override
    public Rating loadRatingById(Long id) {
        return null;
    }
}
