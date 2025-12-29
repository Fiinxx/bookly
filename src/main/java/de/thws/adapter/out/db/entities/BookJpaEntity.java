package de.thws.adapter.out.db.entities;

import de.thws.domain.model.Rating;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "books")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookJpaEntity {

@Id
    private String isbn;
    private String title;
    private String author;
    private int pagecount;
    private String publisher;
    private String genre;
    private double price;
    private String language;
    private String description;
    private String publishingDate;
    private List<Rating> ratings;

}
