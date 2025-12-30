package de.thws.domain.port.in;

import de.thws.domain.model.Rating;

import java.util.List;

public interface LoadRatingUseCase {

    List<Rating> loadAllRatings();
    Rating loadRatingById(Long id);
}
