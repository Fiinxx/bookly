package de.thws.domain.service;

import de.thws.domain.exception.EntityNotFoundException;
import de.thws.domain.model.Rating;
import de.thws.domain.model.RatingFilter;
import de.thws.domain.port.in.LoadRatingUseCase;
import de.thws.domain.port.out.ReadRatingPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class LoadRatingService implements LoadRatingUseCase {
    @Inject
    ReadRatingPort readRatingPort;

    @Override
    public List<Rating> loadAllRatings(RatingFilter filter, int pageIndex, int pageSize)
    {
        return readRatingPort.readAllRatings(filter, pageIndex, pageSize);
    }

    @Override
    public Rating loadRatingById(Long id) {
        return readRatingPort.readRatingById(id).orElseThrow(() -> new EntityNotFoundException("Rating with id " + id + " not found"));
    }
}
