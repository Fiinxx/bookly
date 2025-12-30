package de.thws.domain.model;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Rating {
    private long id;
    private int rating;
    private String comment;
    private String creationTime;


}
