package de.thws.domain.model;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@Data
@RequiredArgsConstructor
public class Rating {
    private Long id;
    private int rating;
    private String comment;
    private Instant creationTime;


}
