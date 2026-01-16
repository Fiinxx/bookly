package de.thws.domain.port.out;

import de.thws.domain.model.Book;

import java.util.Optional;

public interface FetchBookDetailsPort {
    Optional<Book> fetchDetails(String isbn);
}
