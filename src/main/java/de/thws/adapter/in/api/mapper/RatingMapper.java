package de.thws.adapter.in.api.mapper;

import de.thws.adapter.in.api.dto.RatingDTO;
import de.thws.domain.model.Rating;

import java.util.List;
import java.util.stream.Collectors;

public class RatingMapper
{
    public RatingDTO mapToApiModel(Rating rating)
    {
        final var returnValue = new RatingDTO();

        returnValue.setId( rating.getId());
        returnValue.setRating( rating.getRating());
        returnValue.setComment( rating.getComment());
        returnValue.setCreationTime( rating.getCreationTime());

        return returnValue;
    }

    public List<RatingDTO> mapToApiModels(List<Rating> resultList )
    {
        return resultList.stream( ).map( this::mapToApiModel ).collect( Collectors.toList( ) );
    }

    public Rating mapToDomainModel(RatingDTO ratingDTO)
    {
        final var returnValue = new Rating();

        returnValue.setId( ratingDTO.getId());
        returnValue.setRating( ratingDTO.getRating());
        returnValue.setComment( ratingDTO.getComment());
        returnValue.setCreationTime( ratingDTO.getCreationTime());

        return returnValue;
    }
}





