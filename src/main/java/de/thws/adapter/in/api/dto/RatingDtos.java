package de.thws.adapter.in.api.dto;

import jakarta.validation.constraints.PositiveOrZero;

public class RatingDtos {
    public record Detail(
        @PositiveOrZero
        long id,
        int rating,
        String comment,
        String creationTime
    ){}
}
