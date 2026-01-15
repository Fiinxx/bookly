package de.thws.adapter.in.api.dto;

import jakarta.ws.rs.QueryParam;
import lombok.Data;

import java.time.Instant;

@Data
public class RatingFilterDto {

    @QueryParam("rating")
    private int rating;

    @QueryParam("createdAfter")
    private Instant createdAfter;

    @QueryParam("createdBefore")
    private Instant createdBefore;

    @QueryParam("bookId")
    private Long bookId;

    @QueryParam("userId")
    private Long userId;
}
