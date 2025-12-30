package de.thws.domain.port.out;

import de.thws.domain.model.Rating;

import java.util.List;

public interface ReadRatingPort {
    Rating readRatingById(String id);
    List<Rating> readAllRatings();
}
