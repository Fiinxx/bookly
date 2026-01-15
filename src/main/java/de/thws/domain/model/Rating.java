package de.thws.domain.model;
import de.thws.domain.exception.BusinessRuleViolationException;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class Rating {
    private Long id;
    private int rating;
    private String comment;
    private Instant creationTime;

    private Long userId;
    private Long bookId;

    public void setRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new BusinessRuleViolationException("Rating must be between 1 and 5");
        }
        this.rating = rating;
    }
}
