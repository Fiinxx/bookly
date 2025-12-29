package de.thws.domain.port.out;

import de.thws.domain.model.Book;

import java.util.List;

public interface ReadBookPort {
    Book readAllBooks();
    List<Book> readAllBooks(String id);
}
