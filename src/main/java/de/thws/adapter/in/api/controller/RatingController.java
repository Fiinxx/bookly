package de.thws.adapter.in.api.controller;


import de.thws.adapter.in.api.dto.RatingDtos;
import de.thws.adapter.in.api.dto.RatingFilterDto;
import de.thws.adapter.in.api.mapper.RatingMapper;
import de.thws.domain.model.Rating;
import de.thws.domain.model.User;
import de.thws.domain.port.in.CreateRatingUseCase;
import de.thws.domain.port.in.LoadRatingUseCase;
import de.thws.domain.port.in.LoadUserUseCase;
import de.thws.domain.port.in.UpdateRatingUseCase;
import io.quarkus.hal.HalCollectionWrapper;
import io.quarkus.hal.HalEntityWrapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static de.thws.adapter.in.api.utils.PageUriBuilder.buildPageUri;

@Path("ratings")
@ApplicationScoped
public class RatingController
{
    @Inject
    SecurityContext securityContext;

    @Inject
    private UpdateRatingUseCase updateRatingUseCase;

    @Inject
    private LoadRatingUseCase loadRatingUseCase;

    @Inject
    private CreateRatingUseCase createRatingUseCase;

    @Inject
    private LoadUserUseCase loadUserUseCase;

    @Inject
    RatingMapper ratingMapper;

    @Context
    UriInfo uriInfo;

    @Path("{id}")
    @GET
    @Produces( { MediaType.APPLICATION_JSON})
    public Response getRatingById(@Positive @PathParam("id") long id)
    {
        final var domainRating = loadRatingUseCase.loadRatingById(id);
        HalEntityWrapper<RatingDtos.Detail> result = createRatingWrapper(domainRating);
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
        List<HalEntityWrapper<RatingDtos.Detail>> halEntities = domainRatings.stream()
                .map(this::createRatingWrapper)
                .toList();

        HalCollectionWrapper<RatingDtos.Detail> result = new HalCollectionWrapper<>(
                halEntities,
                "ratings");

        result.addLinks(Link.fromUri(buildPageUri(uriInfo, pageIndex, pageSize)).rel("self").build());

        if (pageIndex > 1) result.addLinks(Link.fromUri(buildPageUri(uriInfo, pageIndex - 1, pageSize)).rel("prev").build());
        if (hasNext) result.addLinks(Link.fromUri(buildPageUri(uriInfo, pageIndex + 1, pageSize)).rel("next").build());

        return Response.ok(result).build();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createRating(@Valid RatingDtos.Create ratingDto) throws URISyntaxException {
        String username = securityContext.getUserPrincipal().getName();
        User user = loadUserUseCase.loadUserByUsername(username);
        final var domainRating = this.ratingMapper.toDomain(ratingDto);
        domainRating.setUserId(user.getId());
        this.createRatingUseCase.createRating(domainRating);
        HalEntityWrapper<RatingDtos.Detail> result = createRatingWrapper(domainRating);
        URI selfUri = new URI(result.getLinks().get("self").getHref());
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

    //HELPERS ---------------------

    private HalEntityWrapper<RatingDtos.Detail> createRatingWrapper(Rating domainRating) {
        var dto = ratingMapper.toDetail(domainRating);
        var wrapper = new HalEntityWrapper<>(dto);

        addLinks(wrapper, domainRating);
        return wrapper;
    }

    private void addLinks(HalEntityWrapper<RatingDtos.Detail> wrapper, Rating rating) {
        // 1. Self Link
        URI selfUri = uriInfo.getBaseUriBuilder()
                .path(RatingController.class)
                .path(Long.toString(rating.getId()))
                .build();
        wrapper.addLinks(Link.fromUri(selfUri).rel("self").build());

        // 2. Book Link
        if (rating.getBookId() != null) {
            URI bookUri = uriInfo.getBaseUriBuilder()
                    .path(BookController.class)
                    .path(Long.toString(rating.getBookId()))
                    .build();
            wrapper.addLinks(Link.fromUri(bookUri).rel("book").build());
        }

        // 3. User Link
        if (rating.getUserId() != null) {
            URI userUri = uriInfo.getBaseUriBuilder()
                    .path(UserController.class)
                    .path(Long.toString(rating.getUserId()))
                    .build();
            wrapper.addLinks(Link.fromUri(userUri).rel("user").build());
        }
    }
}
