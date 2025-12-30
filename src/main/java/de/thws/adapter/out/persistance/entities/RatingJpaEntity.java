package de.thws.adapter.out.persistance.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ratings")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RatingJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    int rating;
    String comment;
    String creationTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserJpaEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_isbn")
    private BookJpaEntity book;

}

