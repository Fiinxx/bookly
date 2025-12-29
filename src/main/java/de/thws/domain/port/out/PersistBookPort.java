package de.thws.domain.port.out;

import de.thws.domain.model.Book;

public interface PersistBookPort {
    void persistBook(Book book);
}
