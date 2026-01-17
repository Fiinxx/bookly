package de.thws.domain.port.out;

import de.thws.domain.model.Book;

import java.util.List;

public interface PersistBookPort {
    void persistBook(Book book);
    void persistBooks(List<Book> books);
}
