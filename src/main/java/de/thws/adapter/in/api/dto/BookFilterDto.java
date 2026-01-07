package de.thws.adapter.in.api.dto;

import jakarta.ws.rs.QueryParam;
import lombok.Data;

@Data
public class BookFilterDto {

    @QueryParam("title")
    private String title;

    @QueryParam("isbn")
    private String isbn;

    @QueryParam("author")
    private String author;

    @QueryParam("publisher")
    private String publisher;

    @QueryParam("genre")
    private String genre;


    //soll hiernach sortiert werden können?
    //private int pagecount;
    //private double price;
    //private String language;
    //private String publishingDate;
}
