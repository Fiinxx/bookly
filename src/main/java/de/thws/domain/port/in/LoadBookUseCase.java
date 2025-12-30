package de.thws.domain.port.in;

import de.thws.domain.model.Book;

import java.util.List;

public interface LoadBookUseCase {

    List<Book> loadAllBooks();
    Book loadBookbyId(Long id);
}
