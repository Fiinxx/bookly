package de.thws.adapter.out.persistance.mapper;

import de.thws.adapter.out.persistance.entities.RatingJpaEntity;
import de.thws.domain.model.Rating;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "jakarta", implementationName = "PersistenceRatingMapperImpl")
public interface RatingMapper
{
    RatingJpaEntity toJpaEntity(Rating rating);
    List<Rating> toDomainModels(List<RatingJpaEntity> resultList );
    Rating toDomainModel(RatingJpaEntity jpaEntity);
}
