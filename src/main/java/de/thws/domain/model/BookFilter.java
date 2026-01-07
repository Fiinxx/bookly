package de.thws.domain.model;

import lombok.Data;

@Data
public class BookFilter {

    private String title;
    private String isbn;
    private String author;
    private String publisher;
    private String genre;
}
