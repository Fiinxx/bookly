package de.thws.domain.port.out;

import de.thws.domain.model.Rating;

import java.util.List;
import java.util.Optional;

public interface ReadRatingPort {
    Optional<Rating> readRatingById(Long id);
    List<Rating> readAllRatings();
}
