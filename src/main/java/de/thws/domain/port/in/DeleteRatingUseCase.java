package de.thws.domain.port.in;

import de.thws.domain.model.Rating;

public interface DeleteRatingUseCase {

    void deleteRating(Rating rating);
}
