package de.thws.adapter.in.api.exception;

import de.thws.adapter.in.api.dto.ErrorDto;
import de.thws.domain.exception.EntityNotFoundException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class EntityNotFoundExceptionHandler implements ExceptionMapper<EntityNotFoundException> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(EntityNotFoundException exception) {
        ErrorDto error = new ErrorDto(
                exception.getMessage(),
                Response.Status.NOT_FOUND.getStatusCode(),
                uriInfo.getPath()
        );

        return Response.status(Response.Status.NOT_FOUND)
                .entity(error)
                .build();
    }
}