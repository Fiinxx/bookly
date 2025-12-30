package de.thws.adapter.in.api.controller;

import de.thws.adapter.in.api.dto.BookDTO;
import de.thws.adapter.in.api.mapper.BookMapper;
import de.thws.domain.port.in.CreateBookUseCase;
import de.thws.domain.port.in.LoadBookUseCase;
import io.quarkus.hal.HalEntityWrapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Link;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

import java.net.URI;

@Path("books")
@ApplicationScoped
public class BookController {

    @Inject
    private LoadBookUseCase loadBookUseCase;

    @Inject
    private CreateBookUseCase createBookUseCase;

    BookMapper bookMapper = new BookMapper();

    @Path("{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getBookById(@Positive @PathParam( "id" ) long id) {
        final var domainBook = this.loadBookUseCase.loadBookbyId(id);
        if (domainBook == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        final var apiBook = bookMapper.mapToApiModel(domainBook);
        HalEntityWrapper<BookDTO> result = new HalEntityWrapper<>(apiBook);
        Link selflink = Link.fromUri("/books/" + id)
                .rel("self")
                .build();
        result.addLinks(selflink);
        return Response.ok(result).build();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createBook(@Valid BookDTO bookDTO) {
        final var domainBook = this.bookMapper.mapToDomainModel(bookDTO);
        this.createBookUseCase.createBook(domainBook);
        final var apiBook = bookMapper.mapToApiModel(domainBook);
        HalEntityWrapper<BookDTO> result = new HalEntityWrapper<>(apiBook);

        URI selfUri = UriBuilder.fromResource(BookController.class)
                .path(Long.toString(apiBook.getId()))
                .build();
        Link selfLink = Link.fromUri(selfUri)
                .rel("self")
                .build();
        result.addLinks(selfLink);

        return Response.created(selfUri).entity(result).build();
    }

    //@GET
    //@Produces({MediaType.APPLICATION_JSON})



}
