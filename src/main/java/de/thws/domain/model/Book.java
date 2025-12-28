package de.thws.domain.model;

import lombok.Data;

import java.util.List;


@Data
public class Book {
    String isbn;
    String title;
    String author;
    int pagecount;
    String publisher;
    String genre;
    double price;
    String language;
    String description;
    String publishingDate;
    List<Rating> ratings;

public Book(String isbn, String title, String author, int pagecount, String publisher, String genre, double price, String language, String description, String publishingDate, List<Rating> ratings){
    this.isbn = isbn;
    this.title = title;
    this.author = author;
    this.pagecount = pagecount;
    this.publisher = publisher;
    this.genre = genre;
    this.price = price;
    this.language = language;
    this.description = description;
    this.publishingDate = publishingDate;
    this.ratings = ratings;
}}
