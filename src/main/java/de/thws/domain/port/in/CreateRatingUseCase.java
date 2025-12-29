package de.thws.domain.port.in;

import de.thws.domain.model.Rating;

public interface CreateRatingUseCase {
    void rate(Rating rating);
}
