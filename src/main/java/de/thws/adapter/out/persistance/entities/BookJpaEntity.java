package de.thws.adapter.out.persistance.entities;

import de.thws.domain.model.Rating;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
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

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RatingJpaEntity> ratings = new ArrayList<>();

}
