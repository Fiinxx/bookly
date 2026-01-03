package de.thws.domain.port.in;

import de.thws.domain.model.Book;

import java.util.List;

public interface CreateBookUseCase {

    void createBook(Book book);

    void bulkAddBooks(List<Book> books);

}
