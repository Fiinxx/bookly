package de.thws.adapter.in.api.controller;

import de.thws.adapter.in.api.dto.BookDtos;
import de.thws.adapter.in.api.dto.BookFilterDto;
import de.thws.adapter.in.api.mapper.BookMapper;
import de.thws.domain.model.Book;
import de.thws.domain.port.in.CreateBookUseCase;
import de.thws.domain.port.in.LoadBookUseCase;
import de.thws.domain.port.in.UpdateBookUseCase;
import de.thws.domain.port.in.DeleteBookUseCase;
import io.quarkus.hal.HalCollectionWrapper;
import io.quarkus.hal.HalEntityWrapper;
import de.thws.adapter.in.api.utils.SecurityCheck;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.net.URISyntaxException;
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
    private DeleteBookUseCase deleteBookUseCase;

    @Inject
    private BookMapper bookMapper;

    @Inject
    private SecurityCheck securityCheck;

    @Inject
    private SecurityContext securityContext;

    @Context
    UriInfo uriInfo;

    @Path("{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getBookById(@Positive @PathParam("id") long id) {
        final var domainBook = this.loadBookUseCase.loadBookbyId(id);
        HalEntityWrapper<BookDtos.Detail> result = createBookWrapper(domainBook);
        return Response.ok(result).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllBooks(
            @BeanParam BookFilterDto filter,
            @Positive @DefaultValue("1") @QueryParam("page") int pageIndex,
            @Positive @DefaultValue("20") @QueryParam("size") int pageSize,
            @Context UriInfo uriInfo
    ) {
        var criteria = this.bookMapper.toDomain(filter);
        final var domainBooks = this.loadBookUseCase.loadAllBooks(criteria, pageIndex, pageSize);
        final var apiBooks = this.bookMapper.toDetails(domainBooks);
        boolean hasNext = apiBooks.size() > pageSize;
        if (hasNext) {
            apiBooks.removeLast();
        }

        List<HalEntityWrapper<BookDtos.Detail>> halEntities = domainBooks.stream()
                .map(this::createBookWrapper)
                .toList();


        HalCollectionWrapper<BookDtos.Detail> result = new HalCollectionWrapper<>(
                halEntities,
                "books");

        result.addLinks(Link.fromUri(buildPageUri(uriInfo, pageIndex, pageSize)).rel("self").build());

        if (pageIndex > 1)
            result.addLinks(Link.fromUri(buildPageUri(uriInfo, pageIndex - 1, pageSize)).rel("prev").build());
        if (hasNext) result.addLinks(Link.fromUri(buildPageUri(uriInfo, pageIndex + 1, pageSize)).rel("next").build());

        return Response.ok(result).build();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @RolesAllowed("ADMIN")
    public Response createBook(@Valid BookDtos.Create bookDto) throws URISyntaxException {
        final var domainBook = this.bookMapper.toDomain(bookDto);
        this.createBookUseCase.createBook(domainBook);
        HalEntityWrapper<BookDtos.Detail> result = createBookWrapper(domainBook);
        URI selfUri = new URI(result.getLinks().get("self").getHref());
        return Response.created(selfUri).entity(result).build();
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @RolesAllowed("ADMIN")
    public Response updateBook(
            @Positive @PathParam("id") long id,
            @Valid BookDtos.Detail bookDto) {
        final var domainBook = this.bookMapper.toDomain(bookDto);
        domainBook.setId(id);
        this.updateBookUseCase.updateBook(domainBook);
        HalEntityWrapper<BookDtos.Detail> result = createBookWrapper(domainBook);


        return Response.ok(result).build();
    }
    @DELETE
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response deleteBook(@PathParam("id") long id) {
        if (!securityCheck.isAuthorizedOrAdmin(securityContext, id)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        boolean deleted = deleteBookUseCase.deleteBookById(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }


    //HELPER ---------------------------------
    private HalEntityWrapper<BookDtos.Detail> createBookWrapper(Book domainBook) {
        var dto = bookMapper.toDetail(domainBook);
        var wrapper = new HalEntityWrapper<>(dto);
        addLinks(wrapper, domainBook);
        return wrapper;
    }

    //Selflink
    private void addLinks(HalEntityWrapper<BookDtos.Detail> wrapper, Book book) {
        // 1. Self Link
        URI selfUri = uriInfo.getBaseUriBuilder()
                .path(BookController.class)
                .path(Long.toString(book.getId()))
                .build();
        wrapper.addLinks(Link.fromUri(selfUri).rel("self").build());
        //RatingsLinks
        URI ratingsUri = uriInfo.getBaseUriBuilder()
                .path(RatingController.class)
                .queryParam("bookId", book.getId())
                .build();

        wrapper.addLinks(
                Link.fromUri(ratingsUri)
                        .rel("ratings")
                        .build());
    }
}


        //@GET
    //@Produces({MediaType.APPLICATION_JSON})




