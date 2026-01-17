package de.thws.adapter.in.api.controller;

import de.thws.adapter.in.api.dto.UserDtos;
import de.thws.adapter.in.api.mapper.UserMapper;
import de.thws.adapter.in.api.utils.SecurityCheck;
import de.thws.domain.model.Role;
import de.thws.domain.model.User;
import de.thws.domain.port.in.CreateUserUseCase;
import de.thws.domain.port.in.DeleteUserUseCase;
import de.thws.domain.port.in.LoadUserUseCase;
import de.thws.domain.port.in.UpdateUserUseCase;
import io.quarkus.hal.HalEntityWrapper;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.net.URISyntaxException;

@Path("users")
@ApplicationScoped
public class UserController {

    @Inject
    SecurityContext securityContext;

    @Inject
    private LoadUserUseCase loadUserUseCase;

    @Inject
    private UserMapper userMapper;

    @Inject
    private CreateUserUseCase createUserUseCase;

    @Inject
    private UpdateUserUseCase updateUserUseCase;

    @Inject
    private DeleteUserUseCase deleteUserUseCase;

    @Inject
    private SecurityCheck securityCheck;

    @Context
    UriInfo uriInfo;


    @Path("{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getUserById(@PathParam("id") long id) {
        final var domainUser = loadUserUseCase.loadUserById(id);
        HalEntityWrapper<UserDtos.Detail> result = createUserWrapper(domainUser);
        return Response.ok(result).build();
    }

    //Admin User Registration
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("ADMIN")
    public Response createUser(@Valid UserDtos.Create userDto) throws URISyntaxException {
        final var domainUser = this.userMapper.toDomain(userDto);
        this.createUserUseCase.createUser(domainUser);
        HalEntityWrapper<UserDtos.Detail> result = createUserWrapper(domainUser);
        URI selfUri = new URI(result.getLinks().get("self").getHref());
        return Response.created(selfUri).entity(result).build();
    }

    //PUBLIC User Registration
    @POST
    @Path("register")
    @PermitAll
    public Response registerUser(@Valid UserDtos.Create userDto) throws URISyntaxException {
        final var domainUser = this.userMapper.toDomain(userDto);
        domainUser.setRole(Role.USER);
        this.createUserUseCase.createUser(domainUser);
        HalEntityWrapper<UserDtos.Detail> result = createUserWrapper(domainUser);
        URI selfUri = new URI(result.getLinks().get("self").getHref());
        return Response.created(selfUri).entity(result).build();
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response updateUser(
            @Valid UserDtos.Update userDto,
            @PathParam("id") long id) throws URISyntaxException {
        if (!securityCheck.isAuthorized(securityContext, id)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        final var domainUser = this.userMapper.toDomain(userDto);
        domainUser.setId(id);
        final var updatedDomainUser = this.updateUserUseCase.updateUser(domainUser);
        HalEntityWrapper<UserDtos.Detail> result = createUserWrapper(updatedDomainUser);
        URI selfUri = new URI(result.getLinks().get("self").getHref());
        return Response.created(selfUri).entity(result).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response deleteUser(@PathParam("id") long id){
        if (!securityCheck.isAuthorizedOrAdmin(securityContext, id)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        boolean deleted = deleteUserUseCase.deleteUserById(id);
        if(!deleted){//TODO: replace by Exception
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
    //HELPER ---------------------------------
    private HalEntityWrapper<UserDtos.Detail> createUserWrapper(User domainUser) {
        var dto = userMapper.toDetail(domainUser);
        var wrapper = new HalEntityWrapper<>(dto);
        addLinks(wrapper, domainUser);
        return wrapper;
    }

    private void addLinks(HalEntityWrapper<UserDtos.Detail> wrapper, User user) {
        // 1. Self Link
        URI selfUri = uriInfo.getBaseUriBuilder()
                .path(UserController.class)
                .path(Long.toString(user.getId()))
                .build();
        wrapper.addLinks(Link.fromUri(selfUri).rel("self").build());
        //RatingsLinks
        URI ratingsUri = uriInfo.getBaseUriBuilder()
                .path(RatingController.class)
                .queryParam("userId", user.getId())
                .build();

        wrapper.addLinks(
                Link.fromUri(ratingsUri)
                        .rel("ratings")
                        .build());
    }
}
