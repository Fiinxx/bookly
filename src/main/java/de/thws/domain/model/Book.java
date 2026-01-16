package de.thws.domain.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;




@Data
@RequiredArgsConstructor
public class Book {
    private Long id;
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
    private double averageRating;
    private Long ratingCount;

}
