package de.thws.adapter.in.api.controller;

import de.thws.adapter.in.api.dto.BookDtos;
import de.thws.adapter.in.api.dto.BookFilterDto;
import de.thws.adapter.in.api.mapper.BookMapper;
import de.thws.domain.model.Book;
import de.thws.domain.model.Role;
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
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.StringReader;

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
    public Response getBookById(@Positive @PathParam("id") long id, @Context Request request)
    {
        final var domainBook = this.loadBookUseCase.loadBookbyId(id);
        EntityTag etag = new EntityTag(Integer.toString(domainBook.hashCode()));
        Response.ResponseBuilder builder = request.evaluatePreconditions(etag);
        if (builder != null)
        {
            return builder.build();
        }
        HalEntityWrapper<BookDtos.Detail> result = createBookWrapper(domainBook);
        result.addLinks(Link.fromUri(uriInfo.getBaseUriBuilder().path(BookController.class).build()).rel("collection").build());
        result.addLinks(Link.fromUri(uriInfo.getBaseUriBuilder().path(RatingController.class).queryParam("bookId", id).build()).rel("rate").build());
        if(securityContext.isUserInRole(Role.ADMIN.toString()))
        {
            result.addLinks(Link.fromUri(uriInfo.getBaseUriBuilder().path(BookController.class).path(String.valueOf(id)).build()).rel("update").build());
            result.addLinks(Link.fromUri(uriInfo.getBaseUriBuilder().path(BookController.class).path(String.valueOf(id)).build()).rel("delete").build());
        }

        return Response.ok(result).tag(etag).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllBooks(
            @BeanParam BookFilterDto filter,
            @Positive @DefaultValue("1") @QueryParam("page") int pageIndex,
            @Positive @DefaultValue("20") @QueryParam("size") int pageSize,
            @Context UriInfo uriInfo)
    {
        var criteria = this.bookMapper.toDomain(filter);
        final var domainBooks = this.loadBookUseCase.loadAllBooks(criteria, pageIndex, pageSize);
        final var apiBooks = this.bookMapper.toDetails(domainBooks);
        boolean hasNext = apiBooks.size() > pageSize;
        if (hasNext)
        {
            apiBooks.removeLast();
        }

        List<HalEntityWrapper<BookDtos.Detail>> halEntities = domainBooks.stream()
                .map(this::createBookWrapper)
                .toList();


        HalCollectionWrapper<BookDtos.Detail> result = new HalCollectionWrapper<>(
                halEntities,
                "books");

        //LINKS
        result.addLinks(Link.fromUri(buildPageUri(uriInfo, pageIndex, pageSize)).rel("self").build());//selfe
        if (pageIndex > 1)
            result.addLinks(Link.fromUri(buildPageUri(uriInfo, pageIndex - 1, pageSize)).rel("prev").build());//prev
        if (hasNext) result.addLinks(Link.fromUri(buildPageUri(uriInfo, pageIndex + 1, pageSize)).rel("next").build());//next
        String searchHref = uriInfo.getBaseUriBuilder()
                .path(BookController.class)
                .build()
                .toString() + "{?title,isbn,author,publisher,genre}";
        result.addLinks(
                Link.fromUri(searchHref)
                        .rel("search")
                        .build());//searchtemplate
        if (securityContext.isUserInRole(Role.ADMIN.toString()))
        {
            result.addLinks(Link.fromUri(uriInfo.getBaseUriBuilder().path(BookController.class).build()).rel("create").type(MediaType.APPLICATION_JSON).build());//create
            result.addLinks(Link.fromUri(uriInfo.getBaseUriBuilder().path(BookController.class).build()).rel("bulk-create").type("text/csv").build());//create
        }
        CacheControl cc = new CacheControl();
        cc.setMaxAge(60);
        cc.setPrivate(false);


        return Response.ok(result).cacheControl(cc).build();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @RolesAllowed("ADMIN")
    public Response createBook(@Valid BookDtos.Create bookDto) throws URISyntaxException {//TODO:get rid of this
        final var domainBook = this.bookMapper.toDomain(bookDto);
        this.createBookUseCase.createBook(domainBook);
        HalEntityWrapper<BookDtos.Detail> result = createBookWrapper(domainBook);
        URI selfUri = new URI(result.getLinks().get("self").getHref());
        return Response.created(selfUri).entity(result).build();
    }

    @POST
    @Consumes("text/csv")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("ADMIN")
    public Response createBooksFromCsv(String csvData)
    {
        List<BookDtos.Create> csvDtos = parseCsv(csvData);
        List<Book> domainBooks = this.bookMapper.toDomains(csvDtos);
        this.createBookUseCase.bulkAddBooks(domainBooks);
        List<HalEntityWrapper<BookDtos.Detail>> halEntities = domainBooks.stream()
                .map(this::createBookWrapper)
                .toList();


        HalCollectionWrapper<BookDtos.Detail> result = new HalCollectionWrapper<>(
                halEntities,
                "books");
        return Response.ok(result).build();
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @RolesAllowed("ADMIN")
    public Response updateBook(
            @Positive @PathParam("id") long id,
            @Valid BookDtos.Create bookDto,
            @Context Request request)
    {
        //Lost Update Problem verhindern
        final var existingBook = this.loadBookUseCase.loadBookbyId(id);
        EntityTag etag = new EntityTag(Integer.toString(existingBook.hashCode()));
        Response.ResponseBuilder builder = request.evaluatePreconditions(etag);
        // Wenn builder nicht null ist, stimmen die Etags NICHT überein
        if (builder != null)
        {
            // Gibt automatisch 412 Precondition Failed zurück
            return builder.build();
        }
        //Updaten
        final var domainBook = this.bookMapper.toDomain(bookDto);
        domainBook.setId(id);
        final var updatedDomainBook = this.updateBookUseCase.updateBook(domainBook);
        EntityTag newEtag = new EntityTag(Integer.toString(updatedDomainBook.hashCode()));
        return Response.ok(createBookWrapper(updatedDomainBook)).tag(newEtag).build();
    }
    @DELETE
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response deleteBook(@PathParam("id") long id)
    {
        if (!securityCheck.isAuthorizedOrAdmin(securityContext, id))
        {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        deleteBookUseCase.deleteBookById(id);
        return Response.noContent().build();
    }


    //HELPER ---------------------------------
    private HalEntityWrapper<BookDtos.Detail> createBookWrapper(Book domainBook)
    {
        var dto = bookMapper.toDetail(domainBook);
        var wrapper = new HalEntityWrapper<>(dto);
        addLinks(wrapper, domainBook);
        return wrapper;
    }

    //Selflink
    private void addLinks(HalEntityWrapper<BookDtos.Detail> wrapper, Book book)
    {
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

    private List<BookDtos.Create> parseCsv(String csvData)
    {
        try (StringReader reader = new StringReader(csvData))
        {
            CsvToBean<BookDtos.Create> csvReader = new CsvToBeanBuilder<BookDtos.Create>(reader)
                    .withType(BookDtos.Create.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            return csvReader.parse();
        } catch (Exception e)
        {
            throw new BadRequestException("Invalid CSV format: " + e.getMessage());
        }
    }
}







