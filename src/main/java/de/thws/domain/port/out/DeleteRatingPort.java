package de.thws.domain.port.out;

import de.thws.domain.model.Rating;

public interface DeleteRatingPort {
    void deleteRating(Rating rating);
}
