package de.thws.domain.service;

import de.thws.domain.model.Rating;
import de.thws.domain.port.in.UpdateRatingUseCase;
import de.thws.domain.port.out.UpdateRatingPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UpdateRatingService implements UpdateRatingUseCase {

    @Inject
    UpdateRatingPort updateRatingPort;

    @Override
    public Rating updateRating(Rating rating) {
        return this.updateRatingPort.updateRating(rating);
    }
}

