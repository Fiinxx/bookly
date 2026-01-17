package de.thws.adapter.out.persistance.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;

@Entity
@Table(name = "ratings",
uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "book_id"})
})
@Data
@NoArgsConstructor()
public class RatingJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    int rating;
    String comment;
    Instant creationTime;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private UserJpaEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    @ToString.Exclude
    private BookJpaEntity book;

}

