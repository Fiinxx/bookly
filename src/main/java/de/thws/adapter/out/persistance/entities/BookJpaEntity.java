package de.thws.adapter.out.persistance.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor()
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
    @ToString.Exclude
    private List<RatingJpaEntity> ratings = new ArrayList<>();

    public double getAverageRating() {
        if (ratings == null || ratings.isEmpty()) {
            return 0.0;
        }
        return ratings.stream()
                .mapToInt(RatingJpaEntity::getRating)
                .average()
                .orElse(0.0);
    }

    public Long getRatingCount() {
        if (ratings == null || ratings.isEmpty()) {
            return 0L;
        }
        return Integer.toUnsignedLong(ratings.size());
    }

}
