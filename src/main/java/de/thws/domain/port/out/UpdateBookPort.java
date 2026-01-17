package de.thws.domain.port.out;

import de.thws.domain.model.Book;

public interface UpdateBookPort {
    Book updateBook(Book book);
}
