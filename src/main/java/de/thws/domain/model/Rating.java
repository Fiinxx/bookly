package de.thws.domain.model;
import lombok.Data;

@Data
public class Rating {
    String id;
    int rating;
    String comment;
    String creationTime;

    public Rating(){
        super();
    }

    public Rating(String id, int rating, String comment, String creationTime) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.creationTime = creationTime;
    }
}
