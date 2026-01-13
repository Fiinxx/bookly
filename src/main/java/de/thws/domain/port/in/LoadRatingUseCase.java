package de.thws.domain.port.in;

import de.thws.domain.model.BookFilter;
import de.thws.domain.model.Rating;
import de.thws.domain.model.RatingFilter;

import java.util.List;

public interface LoadRatingUseCase {

    List<Rating> loadAllRatings(RatingFilter filter, int pageIndex, int pageSize);
    Rating loadRatingById(Long id);
}
