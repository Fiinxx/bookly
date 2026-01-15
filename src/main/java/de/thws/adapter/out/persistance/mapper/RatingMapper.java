package de.thws.adapter.out.persistance.mapper;

import de.thws.adapter.out.persistance.entities.BookJpaEntity;
import de.thws.adapter.out.persistance.entities.RatingJpaEntity;
import de.thws.adapter.out.persistance.entities.UserJpaEntity;
import de.thws.domain.model.Rating;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "jakarta", implementationName = "PersistenceRatingMapperImpl")
public abstract class RatingMapper
{
    @Inject
    EntityManager entityManager;

    @Mapping(target = "user", source = "userId")
    @Mapping(target = "book", source = "bookId")
    public abstract RatingJpaEntity toJpaEntity(Rating rating);
    public abstract List<Rating> toDomainModels(List<RatingJpaEntity> resultList );
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "bookId", source = "book.id")
    public abstract Rating toDomainModel(RatingJpaEntity jpaEntity);

    UserJpaEntity userFromId(Long id) {
        if (id == null) return null;
        return entityManager.getReference(UserJpaEntity.class, id);
    }

    BookJpaEntity bookFromId(Long id) {
        if (id == null) return null;
        return entityManager.getReference(BookJpaEntity.class, id);
    }
}
