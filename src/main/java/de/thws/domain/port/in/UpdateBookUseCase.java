package de.thws.domain.port.in;

import de.thws.domain.model.Book;

public interface UpdateBookUseCase {

    Book updateBook(Book book);
}
