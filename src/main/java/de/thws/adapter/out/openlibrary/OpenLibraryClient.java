package de.thws.adapter.out.openlibrary;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/search.json")
@RegisterRestClient(baseUri = "https://openlibrary.org")
public interface OpenLibraryClient {

    @GET
    OpenLibraryResponse getBookByIsbn(@QueryParam("isbn") String isbn, @QueryParam("fields") String fields);
}