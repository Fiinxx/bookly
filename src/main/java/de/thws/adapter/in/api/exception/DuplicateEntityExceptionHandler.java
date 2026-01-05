package de.thws.adapter.in.api.exception;

import de.thws.adapter.in.api.dto.ErrorDto;
import de.thws.domain.exception.DuplicateEntityException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class DuplicateEntityExceptionHandler implements ExceptionMapper<DuplicateEntityException> {

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(DuplicateEntityException exception) {
        ErrorDto error = new ErrorDto(
                exception.getMessage(),
                Response.Status.CONFLICT.getStatusCode(), // 409
                uriInfo.getPath()
        );

        return Response.status(Response.Status.CONFLICT)
                .entity(error)
                .build();
    }
}