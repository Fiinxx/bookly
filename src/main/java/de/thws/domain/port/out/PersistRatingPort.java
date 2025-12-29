package de.thws.domain.port.out;

import de.thws.domain.model.Rating;

public interface PersistRatingPort {
    void persistRating(Rating rating);
}
