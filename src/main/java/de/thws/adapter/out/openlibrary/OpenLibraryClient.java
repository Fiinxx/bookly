package de.thws.adapter.out.openlibrary;

import de.thws.adapter.out.openlibrary.dto.OpenLibraryBookDto;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/isbn")
@RegisterRestClient(baseUri = "https://openlibrary.org")
public interface OpenLibraryClient {

    @GET
    @Path("/{isbn}.json")
    OpenLibraryBookDto getBookByIsbn(@PathParam("isbn") String isbn);
}