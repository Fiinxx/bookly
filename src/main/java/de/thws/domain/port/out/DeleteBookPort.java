package de.thws.domain.port.out;

import de.thws.domain.model.Book;

public interface DeleteBookPort {
    void deleteBook(Book book);
}
