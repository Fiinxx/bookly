package de.thws.adapter.in.api.controller;


import de.thws.adapter.in.api.dto.RatingDtos;
import de.thws.adapter.in.api.dto.RatingFilterDto;
import de.thws.adapter.in.api.mapper.RatingMapper;
import de.thws.adapter.in.api.utils.PageUriBuilder;
import de.thws.domain.port.in.CreateRatingUseCase;
import de.thws.domain.port.in.LoadRatingUseCase;
import de.thws.domain.port.in.UpdateRatingUseCase;
import io.quarkus.hal.HalCollectionWrapper;
import io.quarkus.hal.HalEntityWrapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static de.thws.adapter.in.api.utils.PageUriBuilder.buildPageUri;

@Path("ratings")
@ApplicationScoped
public class RatingController
{
    @Inject
    private UpdateRatingUseCase updateRatingUseCase;

    @Inject
    private LoadRatingUseCase loadRatingUseCase;

    @Inject
    private CreateRatingUseCase createRatingUseCase;

    @Inject
    RatingMapper ratingMapper;

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
        final var apiRating = ratingMapper.toDetail(domainRating);
        HalEntityWrapper<RatingDtos.Detail> result = new HalEntityWrapper<>(apiRating);
        Link selflink = Link.fromUri("/ratings/" + id)
                .rel("self")
                .build();
        result.addLinks(selflink);
        return Response.ok(result).build();
    }
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllRatings(
            @BeanParam RatingFilterDto filter,
            @Positive @DefaultValue( "1" ) @QueryParam( "page" ) int pageIndex,
            @Positive @DefaultValue( "20" ) @QueryParam( "size" ) int pageSize,
            @Context UriInfo uriInfo
    )
    {
        var criteria = this.ratingMapper.toDomain(filter);
        final var domainRatings = this.loadRatingUseCase.loadAllRatings(criteria, pageIndex, pageSize);
        final var apiRatings = this.ratingMapper.toDetails(domainRatings);
        boolean hasNext = apiRatings.size() > pageSize;
        if (hasNext) {
            apiRatings.removeLast();
        }
        List<HalEntityWrapper<RatingDtos.Detail>> halEntities = apiRatings.stream()
                .map(rating -> {
                    var wrapper = new HalEntityWrapper<>(rating);

                    URI selfUri = uriInfo.getBaseUriBuilder()
                            .path(RatingController.class)
                            .path(Long.toString(rating.id()))
                            .scheme(null).host(null).port(-1) // Strip host/port
                            .build();

                    wrapper.addLinks(Link.fromUri(selfUri).rel("self").build());

                    return wrapper;
                })
                .toList();

        HalCollectionWrapper<RatingDtos.Detail> result = new HalCollectionWrapper<>(
                halEntities,
                "ratings");

        result.addLinks(Link.fromUri(buildPageUri(uriInfo, pageIndex, pageSize)).rel("self").build());

        if (pageIndex > 1) result.addLinks(Link.fromUri(buildPageUri(uriInfo, pageIndex - 1, pageSize)).rel("prev").build());
        if (hasNext) result.addLinks(Link.fromUri(buildPageUri(uriInfo, pageIndex + 1, pageSize)).rel("next").build());

        return Response.ok(result).build();
    }


    //TODO: user und book relation fehlt noch
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createRating(@Valid RatingDtos.Create ratingDto){
        final var domainRating = this.ratingMapper.toDomain(ratingDto);
        this.createRatingUseCase.createRating(domainRating);
        final var apiRating = this.ratingMapper.toDetail(domainRating);
        HalEntityWrapper<RatingDtos.Detail> result = new HalEntityWrapper<>(apiRating);
        URI selfUri = UriBuilder.fromResource(RatingController.class)
                .path(Long.toString(apiRating.id()))
                .build();
        Link selfLink = Link.fromUri(selfUri)
                .rel("self")
                .build();
        result.addLinks(selfLink);
        return Response.created(selfUri).entity(result).build();
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response updateRating(
            @Positive @PathParam( "id" ) long id,
            @Valid RatingDtos.Detail ratingDto){
        final var domainRating = this.ratingMapper.toDomain(ratingDto);
        domainRating.setId(id);
        this.updateRatingUseCase.updateRating(domainRating);
        return Response.ok().build();
    }
}
