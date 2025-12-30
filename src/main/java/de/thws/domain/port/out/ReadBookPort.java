package de.thws.domain.port.out;

import de.thws.domain.model.Book;

import java.util.List;

public interface ReadBookPort {
    List<Book> readAllBooks();
    Book readBookById(String id);
}
