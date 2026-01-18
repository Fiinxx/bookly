package de.thws.adapter.in.api.controller;

import de.thws.domain.port.in.LoadUserUseCase;
import io.quarkus.hal.HalEntityWrapper;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.*;

import java.net.URI;

@Path("/")
public class DispatcherController {

    @Context
    UriInfo uriInfo;

    @Context
    SecurityContext securityContext;

    @Inject
    LoadUserUseCase loadUserUseCase;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getApiRoot() {
        var rootInfo = new ApiRoot("Bookly API", "1.0.0");
        var wrapper = new HalEntityWrapper<>(rootInfo);

        addLinks(wrapper);

        return Response.ok(wrapper).build();
    }

    private void addLinks(HalEntityWrapper<ApiRoot> wrapper) {
        // self link
        wrapper.addLinks(Link.fromUri(uriInfo.getRequestUri()).rel("self").build());

        // Books Collection
        URI booksUri = uriInfo.getBaseUriBuilder()
                .path(BookController.class)
                .build();
        wrapper.addLinks(Link.fromUri(booksUri).rel("books").build());

        // Ratings Collection
        URI ratingsUri = uriInfo.getBaseUriBuilder()
                .path(RatingController.class)
                .build();
        wrapper.addLinks(Link.fromUri(ratingsUri).rel("ratings").build());

        // Authenticated User Links
        if (securityContext.getUserPrincipal() != null) {
            final var user = loadUserUseCase.loadUserByUsername(securityContext.getUserPrincipal().getName());
            URI meUri = uriInfo.getBaseUriBuilder()
                    .path(UserController.class)
                    .path(Long.toString(user.getId()))
                    .build();
            wrapper.addLinks(Link.fromUri(meUri).rel("me").build());
        }else {
            URI registerUri = uriInfo.getBaseUriBuilder() //TODO: route gets removed remove here
                    .path(UserController.class)
                    .path("register")
                    .build();
            wrapper.addLinks(Link.fromUri(registerUri).rel("register").build());
        }
    }

    public record ApiRoot(String name, String version) {}
}