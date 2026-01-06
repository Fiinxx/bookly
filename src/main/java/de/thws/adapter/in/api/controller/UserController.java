package de.thws.adapter.in.api.controller;

import de.thws.adapter.in.api.dto.UserDtos;
import de.thws.adapter.in.api.mapper.UserMapper;
import de.thws.domain.port.in.LoadUserUseCase;
import io.quarkus.hal.HalEntityWrapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Link;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("users")
@ApplicationScoped
public class UserController {

    @Inject
    private LoadUserUseCase loadUserUseCase;

    @Inject
    private UserMapper userMapper;

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
}
