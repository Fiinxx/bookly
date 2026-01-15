package de.thws.adapter.in.api.controller;

import de.thws.adapter.in.api.dto.BookDtos;
import de.thws.adapter.in.api.dto.BookFilterDto;
import de.thws.adapter.in.api.mapper.BookMapper;
import de.thws.domain.port.in.CreateBookUseCase;
import de.thws.domain.port.in.LoadBookUseCase;
import de.thws.domain.port.in.UpdateBookUseCase;
import io.quarkus.hal.HalCollectionWrapper;
import io.quarkus.hal.HalEntityWrapper;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.util.List;

import static de.thws.adapter.in.api.utils.PageUriBuilder.buildPageUri;

@Path("books")
@ApplicationScoped
public class BookController {

    @Inject
    private UpdateBookUseCase updateBookUseCase;

    @Inject
    private LoadBookUseCase loadBookUseCase;

    @Inject
    private CreateBookUseCase createBookUseCase;

    @Inject
    private BookMapper bookMapper;

    @Path("{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getBookById(@Positive @PathParam( "id" ) long id) {
        final var domainBook = this.loadBookUseCase.loadBookbyId(id);
        final var apiBook = bookMapper.toDetail(domainBook);
        HalEntityWrapper<BookDtos.Detail> result = new HalEntityWrapper<>(apiBook);
        Link selflink = Link.fromUri("/books/" + id)
                .rel("self")
                .build();
        result.addLinks(selflink);
        return Response.ok(result).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllBooks(
            @BeanParam BookFilterDto filter,
            @Positive @DefaultValue( "1" ) @QueryParam( "page" ) int pageIndex,
            @Positive @DefaultValue( "20" ) @QueryParam( "size" ) int pageSize,
            @Context UriInfo uriInfo
            ) {
        var criteria = this.bookMapper.toDomain(filter);
        final var domainBooks = this.loadBookUseCase.loadAllBooks(criteria, pageIndex, pageSize);
        final var apiBooks = this.bookMapper.toDetails(domainBooks);
        boolean hasNext = apiBooks.size() > pageSize;
        if (hasNext) {
            apiBooks.removeLast();
        }

        List<HalEntityWrapper<BookDtos.Detail>> halEntities = apiBooks.stream()
                .map(book -> {
                    var wrapper = new HalEntityWrapper<>(book);

                    URI selfUri = uriInfo.getBaseUriBuilder()
                            .path(BookController.class)
                            .path(Long.toString(book.id()))
                            .scheme(null).host(null).port(-1) // Strip host/port
                            .build();

                    wrapper.addLinks(Link.fromUri(selfUri).rel("self").build());

                    return wrapper;
                })
                .toList();

        HalCollectionWrapper<BookDtos.Detail> result = new HalCollectionWrapper<>(
                halEntities,
                "books");

        result.addLinks(Link.fromUri(buildPageUri(uriInfo, pageIndex, pageSize)).rel("self").build());

        if (pageIndex > 1) result.addLinks(Link.fromUri(buildPageUri(uriInfo, pageIndex - 1, pageSize)).rel("prev").build());
        if (hasNext) result.addLinks(Link.fromUri(buildPageUri(uriInfo, pageIndex + 1, pageSize)).rel("next").build());

        return Response.ok(result).build();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @RolesAllowed("ADMIN")
    public Response createBook(@Valid BookDtos.Create bookDto) {
        final var domainBook = this.bookMapper.toDomain(bookDto);
        this.createBookUseCase.createBook(domainBook);
        final var apiBook = bookMapper.toDetail(domainBook);
        HalEntityWrapper<BookDtos.Detail> result = new HalEntityWrapper<>(apiBook);

        URI selfUri = UriBuilder.fromResource(BookController.class)
                .path(Long.toString(apiBook.id()))
                .build();
        Link selfLink = Link.fromUri(selfUri)
                .rel("self")
                .build();
        result.addLinks(selfLink);

        return Response.created(selfUri).entity(result).build();
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @RolesAllowed("ADMIN")
    public Response updateBook(
            @Positive @PathParam( "id" ) long id,
            @Valid BookDtos.Detail bookDto) {
        final var domainBook = this.bookMapper.toDomain(bookDto);
        domainBook.setId(id);
        this.updateBookUseCase.updateBook(domainBook);
        final var apiBook = bookMapper.toDetail(domainBook);
        HalEntityWrapper<BookDtos.Detail> result = new HalEntityWrapper<>(apiBook);

        URI selfUri = UriBuilder.fromResource(BookController.class)
                .path(Long.toString(apiBook.id()))
                .build();
        Link selfLink = Link.fromUri(selfUri)
                .rel("self")
                .build();
        result.addLinks(selfLink);

        return Response.ok(result).build();

    }

    }

    //@GET
    //@Produces({MediaType.APPLICATION_JSON})




