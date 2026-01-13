package de.thws.domain.model;

import lombok.Data;

import java.time.Instant;

@Data
public class RatingFilter {
    private int rating;
    private Instant creatingTime;
    private Long bookId;
    private Long userId;

}
