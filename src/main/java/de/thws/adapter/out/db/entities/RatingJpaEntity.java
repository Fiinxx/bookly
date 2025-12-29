package de.thws.adapter.out.db.entities;

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
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    int rating;
    String comment;
    String creationTime;



}

