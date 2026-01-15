package de.thws.adapter.in.api.mapper;

import de.thws.adapter.in.api.dto.RatingDtos;
import de.thws.adapter.in.api.dto.RatingFilterDto;
import de.thws.domain.model.Rating;
import de.thws.domain.model.RatingFilter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "jakarta", implementationName = "ApiRatingMapperImpl")
public interface RatingMapper
{
    @Mapping(target = "id", ignore = true)
    Rating toDomain(RatingDtos.Create request);
    RatingDtos.Detail toDetail(Rating rating);
    List<RatingDtos.Detail> toDetails(List<Rating> ratings);
    RatingFilter toDomain(RatingFilterDto ratingFilterDto);
    Rating toDomain(RatingDtos.Detail request);
}





