package de.thws.adapter.in.api.dto;

import de.thws.domain.model.Rating;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.util.List;

@XmlRootElement
@Data
public class BookDTO {
    @PositiveOrZero
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
    private List<Rating> ratings;

}
