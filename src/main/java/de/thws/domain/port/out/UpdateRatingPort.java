package de.thws.domain.port.out;

import de.thws.domain.model.Rating;

public interface UpdateRatingPort {
    Rating updateRating(Rating rating);
}
