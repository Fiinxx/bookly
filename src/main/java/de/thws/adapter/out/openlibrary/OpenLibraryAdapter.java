package de.thws.adapter.out.openlibrary;

import de.thws.adapter.out.openlibrary.dto.OpenLibraryBookDto;
import de.thws.domain.model.Book;
import de.thws.domain.port.out.FetchBookDetailsPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import java.util.List;
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

    // Added logger
    private static final Logger LOG = Logger.getLogger(OpenLibraryAdapter.class);

    @Override
    public Optional<Book> fetchDetails(String isbn) {
        try {
            OpenLibraryResponse response = client.getBookByIsbn(isbn, "title,author_name,number_of_pages_median,publisher,first_sentence,first_publish_year");

            if (response.docs() == null || response.docs().isEmpty()) {
                return Optional.empty();
            }

            Book book = responseToBook(isbn, response);
            return Optional.of(book);

        } catch (Exception e) {
            LOG.error("Failed to map OpenLibrary response for ISBN: " + isbn, e);
            return Optional.empty();
        }
    }

    private static Book responseToBook(String isbn, OpenLibraryResponse response) {
        OpenLibraryBookDto dto = response.docs().getFirst();

        Book book = new Book();
        book.setIsbn(isbn);
        book.setTitle(dto.title());
        if (dto.numberOfPages() != 0) {
            book.setPagecount(dto.numberOfPages());
        }
        if (hasItems(dto.authors())) {
            book.setAuthor(dto.authors().getFirst());
        }
        if (hasItems(dto.publishers())) {
            book.setPublisher(dto.publishers().getFirst());
        }
        if (hasItems(dto.firstSentence())) {
            book.setDescription(dto.firstSentence().getFirst());
        }
        if (dto.publishingYear() != 0) {
            book.setPublishingDate(String.valueOf(dto.publishingYear()));
        }
        return book;
    }

    // Helper
    private static boolean hasItems(List<?> list) {
        return list != null && !list.isEmpty();
    }
}
