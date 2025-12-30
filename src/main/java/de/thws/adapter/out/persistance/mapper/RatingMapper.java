package de.thws.adapter.out.persistance.mapper;

import de.thws.adapter.out.persistance.entities.RatingJpaEntity;
import de.thws.domain.model.Rating;

import java.util.List;
import java.util.stream.Collectors;

public class RatingMapper
{
    public RatingJpaEntity mapToJpaEntity(Rating rating)
    {
        final var returnValue = new RatingJpaEntity();

        returnValue.setId( rating.getId());
        returnValue.setRating( rating.getRating());
        returnValue.setComment( rating.getComment());
        returnValue.setCreationTime( rating.getCreationTime());

        return returnValue;
    }

    public List<Rating> mapToDomainModels(List<RatingJpaEntity> resultList )
    {
        return resultList.stream( ).map( this::mapToDomainModel ).collect( Collectors.toList( ) );
    }

    public Rating mapToDomainModel(RatingJpaEntity jpaEntity)
    {
        final var returnValue = new Rating();

        returnValue.setId( jpaEntity.getId());
        returnValue.setRating( jpaEntity.getRating());
        returnValue.setComment( jpaEntity.getComment());
        returnValue.setCreationTime( jpaEntity.getCreationTime());

        return returnValue;
    }
}
