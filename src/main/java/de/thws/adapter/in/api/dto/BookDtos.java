package de.thws.adapter.in.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;


public class BookDtos {
    // Request
    public record Create(
            @NotBlank(message = "ISBN is mandatory")
            String isbn,
            String title,
            String author,
            @PositiveOrZero
            int pagecount,
            String publisher,
            String genre,
            double price,
            String language,
            String description,
            String publishingDate
    ) {}

    public record Detail(
            long id,
            String isbn,
            String title,
            String author,
            int pagecount,
            String publisher,
            String genre,
            double price,
            String language,
            String description,
            String publishingDate
    ){

    }


//    private long id;
//    private String isbn;
//    private String title;
//    private String author;
//    private int pagecount;
//    private String publisher;
//    private String genre;
//    private double price;
//    private String language;
//    private String description;
//    private String publishingDate;
//    private List<Rating> ratings;

    // Request
    //public record UpdateTitle(String newTitle) {}

    // Response
    //public record Detail(Long id, String title, String isbn, List<Rating> ratings) {}

    // Response
    //public record Summary(Long id, String title) {}
}
