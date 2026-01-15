package de.thws.adapter.in.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.Instant;

public class RatingDtos {
    public record Detail(
        @PositiveOrZero
        long id,
        int rating,
        String comment,
        Instant creationTime
    ){}
    public record Create(
            @Min(value = 1, message = "Rating must be at least 1")
            @Max(value = 5, message = "Rating must be at most 5")
            int rating,
            String comment,

            long bookId
    ){}
}
