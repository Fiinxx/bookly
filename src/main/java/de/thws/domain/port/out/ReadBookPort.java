package de.thws.domain.port.out;

import de.thws.domain.model.Book;

import java.util.List;
import java.util.Optional;

public interface ReadBookPort {
    List<Book> readAllBooks();
    Optional<Book> readBookById(Long id);
}
