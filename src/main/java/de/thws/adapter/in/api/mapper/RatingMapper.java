package de.thws.adapter.in.api.mapper;

import de.thws.adapter.in.api.dto.RatingDtos;
import de.thws.domain.model.Rating;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "jakarta")
public interface RatingMapper
{
    RatingDtos.Detail toDetail(Rating rating);
    List<RatingDtos.Detail> toDetails(List<Rating> ratings);
}





