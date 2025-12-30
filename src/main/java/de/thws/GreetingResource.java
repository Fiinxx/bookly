package de.thws;

import de.thws.domain.model.Book;
import io.quarkus.hal.HalCollectionWrapper;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import io.quarkus.hal.HalEntityWrapper;

import java.util.List;

@Path("/hello")
public class GreetingResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        HalEntityWrapper < Book > wrapper = new HalEntityWrapper<>(new Book());
        List<Book> books = List.of(new Book(), new Book());

        return "Hello from Quarkus REST";

    }


}
