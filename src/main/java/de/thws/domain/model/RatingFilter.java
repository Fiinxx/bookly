package de.thws.domain.model;

import lombok.Data;

import java.time.Instant;

@Data
public class RatingFilter {
    private int rating;
    private Instant createdAfter;
    private Instant createdBefore;
    private Long bookId;
    private Long userId;

}
