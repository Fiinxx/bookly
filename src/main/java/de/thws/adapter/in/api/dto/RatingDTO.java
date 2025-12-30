package de.thws.adapter.in.api.dto;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class RatingDTO {
    @PositiveOrZero
    private long id;
    private int rating;
    private String comment;
    private String creationTime;
}
