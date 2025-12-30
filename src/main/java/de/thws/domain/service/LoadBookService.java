package de.thws.domain.service;

import de.thws.domain.model.Book;
import de.thws.domain.port.in.LoadBookUseCase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
@ApplicationScoped
public class LoadBookService implements LoadBookUseCase {
    @Override
    public List<Book> loadAllBooks() {
        return List.of();
    }

    @Override
    public Book loadBookbyId(Long id) {
        return null;
    }
}
