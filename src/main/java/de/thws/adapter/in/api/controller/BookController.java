package de.thws.adapter.in.api.controller;

import de.thws.adapter.in.api.dto.BookDTO;
import de.thws.adapter.in.api.mapper.BookMapper;
import de.thws.domain.port.in.LoadBookUseCase;
import io.quarkus.hal.HalEntityWrapper;
import jakarta.inject.Inject;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("books")
public class BookController {

    @Inject
    private LoadBookUseCase loadBookUseCase;

    @Path("{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getBook(@Positive @PathParam( "id" ) long id) {
        final var requestedBook = this.loadBookUseCase.loadBookbyId(id);
        if (requestedBook == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        //HalEntityWrapper<BookDTO> halEntityWrapper = new HalEntityWrapper<>();
        return null;
    }


}
