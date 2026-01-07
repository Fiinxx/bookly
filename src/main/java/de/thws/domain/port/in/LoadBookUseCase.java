package de.thws.domain.port.in;

import de.thws.domain.model.Book;
import de.thws.domain.model.BookFilter;

import java.util.List;

public interface LoadBookUseCase {

    List<Book> loadAllBooks(BookFilter filter, int pageIndex, int pageSize);
    Book loadBookbyId(Long id);
}
