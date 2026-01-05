package de.thws.adapter.in.api.exception;

import de.thws.adapter.in.api.dto.ErrorDto;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {

    @Context
    UriInfo uriInfo;

    private static final Logger LOG = Logger.getLogger(GlobalExceptionHandler.class);

    @Override
    public Response toResponse(Exception exception) {
        LOG.error("Failed to process request", exception);

        ErrorDto error = new ErrorDto(
                "An unexpected error occurred. Please contact support.",
                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                uriInfo.getPath()
        );

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(error)
                .build();
    }
}