package de.thws.adapter.in.api.dto;

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
            int rating,
            String comment
    ){};
}
