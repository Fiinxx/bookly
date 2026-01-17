package de.thws.adapter.out.persistance.mapper;

import de.thws.adapter.out.persistance.entities.BookJpaEntity;
import de.thws.adapter.out.persistance.entities.RatingJpaEntity;
import de.thws.adapter.out.persistance.entities.UserJpaEntity;
import de.thws.domain.model.Rating;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

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

    @Mapping(target = "id", ignore = true)           // ID never changes
    @Mapping(target = "creationTime", ignore = true) // Creation time is immutable
    @Mapping(target = "user", ignore = true)         // You cannot change WHO wrote the rating
    @Mapping(target = "book", ignore = true)         // You cannot change WHICH book is rated
    public abstract void updateJpaFromDomain(Rating rating, @MappingTarget RatingJpaEntity entity);

    UserJpaEntity userFromId(Long id) {
        if (id == null) return null;
        return entityManager.getReference(UserJpaEntity.class, id);
    }

    BookJpaEntity bookFromId(Long id) {
        if (id == null) return null;
        return entityManager.getReference(BookJpaEntity.class, id);
    }

}
