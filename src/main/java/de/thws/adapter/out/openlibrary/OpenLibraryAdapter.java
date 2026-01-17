package de.thws.adapter.out.openlibrary;

import de.thws.adapter.out.openlibrary.dto.OpenLibraryBookDto;
import de.thws.domain.model.Book;
import de.thws.domain.port.out.FetchBookDetailsPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.Optional;
@ApplicationScoped
public class OpenLibraryAdapter implements FetchBookDetailsPort {
    /*
        Comments: we know this mapping is not that good (lot of fields still empty and just weird things),
        eg first sentence is the description or just picking the first item.
        But due to time constraints we leaved this definitely not production ready implementation as it is because changing the
        domain model would be too much work now.
        We leave it still here the show that it would be possible
 */

    @Inject
    @RestClient
    OpenLibraryClient client;

    @Override
    public Optional<Book> fetchDetails(String isbn) {
        try {
            // 1. Call the API
            OpenLibraryResponse response = client.getBookByIsbn(isbn, "title,author_name,number_of_pages_median,publisher,first_sentence,first_publish_year");

            // 2. Validate we actually got a result
            if (response.docs() == null || response.docs().isEmpty()) {
                return Optional.empty();
            }

            Book book = responseToBook(isbn, response);

            return Optional.of(book);

        } catch (Exception e) {
            // should be logged but is not due to reasons...
            return Optional.empty();
        }
    }

    private static Book responseToBook(String isbn, OpenLibraryResponse response) {
        OpenLibraryBookDto dto = response.docs().getFirst();

        // 3. Map DTO to Domain Book
        Book book = new Book();
        book.setIsbn(isbn);
        book.setTitle(dto.title());
        book.setPagecount(dto.numberOfPages());
        book.setAuthor(dto.authors().getFirst());
        book.setPublisher(dto.publishers().getFirst());
        book.setDescription(dto.firstSentence().getFirst());
        book.setPublishingDate(String.valueOf(dto.publishingYear()));
        return book;
    }
}
