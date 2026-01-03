package de.thws.adapter.in.api.controller;

import de.thws.adapter.in.api.dto.RatingDTO;
import de.thws.adapter.in.api.mapper.RatingMapper;
import de.thws.domain.port.in.LoadRatingUseCase;
import io.quarkus.hal.HalCollectionWrapper;
import io.quarkus.hal.HalEntityWrapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Link;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Path("ratings")
@ApplicationScoped
public class RatingController
{
    @Inject
    private LoadRatingUseCase loadRatingUseCase;
    //@Inject
    //private CreateRatingUseCase createRatingUseCase;


    RatingMapper ratingMapper = new RatingMapper();

    @Path("{id}")
    @GET
    @Produces( { MediaType.APPLICATION_JSON})
    public Response getRatingById(@Positive @PathParam("id") long id)
    {
        final var domainRating = loadRatingUseCase.loadRatingById(id);
        if(domainRating == null)
        {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        final var apiRating = ratingMapper.mapToApiModel(domainRating);
        HalEntityWrapper<RatingDTO> result = new HalEntityWrapper<>(apiRating);
        Link selflink = Link.fromUri("/ratings/" + id)
                .rel("self")
                .build();
        result.addLinks(selflink);
        return Response.ok(result).build();
    }
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllRatings(
            @DefaultValue( "" ) @QueryParam( "q" ) String query,
            @PositiveOrZero @DefaultValue( "0" ) @QueryParam( "offset" ) long offset,
            @Positive @DefaultValue( "20" ) @QueryParam( "size" ) long size
    )
    {
        final var domainRatings = loadRatingUseCase.loadAllRatings();
        final var apiRatings = ratingMapper.mapToApiModels(domainRatings);

        List<HalEntityWrapper<RatingDTO>> halEntities = apiRatings
                .stream()
                .map(rating -> new HalEntityWrapper<>(rating))
                .collect(Collectors.toList());

        HalCollectionWrapper<RatingDTO> result = new HalCollectionWrapper<>(
                halEntities,
                "ratings",
                Link.fromUri("/ratings").rel("self").build());

        if (offset>0)
        {
            result.addLinks(Link.fromUri(String.format("/ratings?offset=%d&size=%d",
                    Math.max(0, offset-size), size))
                    .rel("prev")
                    .build()
            );
        }
        if (apiRatings.size() == size)
        {
            result.addLinks(Link.fromUri(String.format("ratings?offset=%d&size=%d", offset+size, size))
                    .rel("next")
                    .build()
            );
        }
        return Response.ok(result).build();
    }

    //@POST
    //@Consumes({MediaType.APPLICATION_JSON})
    //public Response
}
