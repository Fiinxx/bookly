package de.thws.adapter.out.openlibrary;

import de.thws.adapter.out.openlibrary.dto.OpenLibraryBookDto;
import de.thws.domain.model.Book;
import de.thws.domain.port.out.FetchBookDetailsPort;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.Optional;

public class OpenLibraryAdapter implements FetchBookDetailsPort {

    @Inject
    @RestClient
    OpenLibraryClient client;

    @Override
    public Optional<Book> fetchDetails(String isbn) {
        try {
            OpenLibraryBookDto dto = client.getBookByIsbn(isbn);

            Book book = new Book();
            book.setIsbn(isbn);
            book.setTitle(dto.title());
            book.setPagecount(dto.numberOfPages());
            book.setPublishingDate(dto.publishDate());
            if (dto.publishers() != null && !dto.publishers().isEmpty()) {
                book.setPublisher(dto.publishers().getFirst());
            }

            return Optional.of(book);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
