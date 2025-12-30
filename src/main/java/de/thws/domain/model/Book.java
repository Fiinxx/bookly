package de.thws.domain.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;


@Data
@RequiredArgsConstructor
public class Book {
    private long id;
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
    private  List<Rating> ratings;
}
