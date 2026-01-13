package de.thws.domain.service;

import de.thws.domain.model.Rating;
import de.thws.domain.port.in.CreateRatingUseCase;
import de.thws.domain.port.out.PersistRatingPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CreateRatingService implements CreateRatingUseCase {

    @Inject
    PersistRatingPort persistRatingPort;

    @Override
    public void createRating(Rating rating) {
        this.persistRatingPort.persistRating(rating);
    }
}
