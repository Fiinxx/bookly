package de.thws.adapter.in.api.dto;

import com.opencsv.bean.CsvBindByName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;


public class BookDtos {
    // Request
    @NoArgsConstructor
    @Data
    public static class Create {

        @CsvBindByName(column = "isbn", required = true)
        @NotBlank(message = "ISBN is mandatory")
        String isbn;

        @CsvBindByName(column = "title")
        String title;

        @CsvBindByName(column = "author")
        String author;

        @CsvBindByName(column = "pagecount")
        @PositiveOrZero
        int pagecount;

        @CsvBindByName(column = "publisher")
        String publisher;

        @CsvBindByName(column = "genre")
        String genre;

        @CsvBindByName(column = "price")
        double price;

        @CsvBindByName(column = "language")
        String language;

        @CsvBindByName(column = "description")
        String description;

        @CsvBindByName(column = "publishingDate")
        String publishingDat;
    }

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
            String publishingDate,
            double averageRating,
            Long ratingCount
    ){}


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
