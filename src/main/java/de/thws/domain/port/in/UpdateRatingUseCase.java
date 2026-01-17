package de.thws.domain.port.in;

import de.thws.domain.model.Rating;

public interface UpdateRatingUseCase {

    Rating updateRating(Rating rating);
}
