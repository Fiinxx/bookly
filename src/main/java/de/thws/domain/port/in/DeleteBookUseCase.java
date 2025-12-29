package de.thws.domain.port.in;

import de.thws.domain.model.Book;

public interface DeleteBookUseCase {

    void deleteBook(Book book);
}
