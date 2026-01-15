package de.thws.adapter.in.api.controller;

import de.thws.adapter.in.api.dto.UserDtos;
import de.thws.adapter.in.api.mapper.UserMapper;
import de.thws.domain.model.Role;
import de.thws.domain.port.in.CreateUserUseCase;
import de.thws.domain.port.in.LoadUserUseCase;
import io.quarkus.hal.HalEntityWrapper;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;

@Path("users")
@ApplicationScoped
public class UserController {

    @Inject
    private LoadUserUseCase loadUserUseCase;

    @Inject
    private UserMapper userMapper;

    @Inject
    private CreateUserUseCase createUserUseCase;

    @Path("{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getUserById(@PathParam("id") long id) {
        final var domainUser = loadUserUseCase.loadUserById(id);
        final var apiUser = this.userMapper.toDetail(domainUser);
        HalEntityWrapper<UserDtos.Detail> result = new HalEntityWrapper<>(apiUser);
        Link selfLink = Link.fromUri("/users/" + id)
                .rel("self")
                .build();
        result.addLinks(selfLink);
        return Response.ok(result).build();
    }

    //Admin User Registration
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("ADMIN")
    public Response createUser(@Valid UserDtos.Create userDto) {
        final var domainUser = this.userMapper.toDomain(userDto);
        this.createUserUseCase.createUser(domainUser);
        final var apiUser = this.userMapper.toDetail(domainUser);
        HalEntityWrapper<UserDtos.Detail> result = new HalEntityWrapper<>(apiUser);
        URI selfUri = UriBuilder.fromResource(UserController.class)
                .path(Long.toString(apiUser.id()))
                .build();
        result.addLinks(Link.fromUri(selfUri).rel("self").build());
        return Response.created(selfUri).entity(result).build();
    }

    //PUBLIC User Registration
    @POST
    @Path("register")
    @PermitAll
    public Response registerUser(@Valid UserDtos.Create userDto) {
        final var domainUser = this.userMapper.toDomain(userDto);
        domainUser.setRole(Role.USER);
        this.createUserUseCase.createUser(domainUser);
        final var apiUser = this.userMapper.toDetail(domainUser);
        HalEntityWrapper<UserDtos.Detail> result = new HalEntityWrapper<>(apiUser);
        URI selfUri = UriBuilder.fromResource(UserController.class)
                .path(Long.toString(apiUser.id()))
                .build();
        result.addLinks(Link.fromUri(selfUri).rel("self").build());
        return Response.created(selfUri).entity(result).build();
    }


}
