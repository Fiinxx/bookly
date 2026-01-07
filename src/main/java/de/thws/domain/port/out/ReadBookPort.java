package de.thws.domain.port.out;

import de.thws.domain.model.Book;
import de.thws.domain.model.BookFilter;

import java.util.List;
import java.util.Optional;

public interface ReadBookPort {
    List<Book> readAllBooks(BookFilter filter, int pageIndex, int pageSize);
    Optional<Book> readBookById(Long id);
}
