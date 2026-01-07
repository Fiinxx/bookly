package de.thws.domain.service;

import de.thws.domain.exception.EntityNotFoundException;
import de.thws.domain.model.Book;
import de.thws.domain.model.BookFilter;
import de.thws.domain.port.in.LoadBookUseCase;
import de.thws.domain.port.out.ReadBookPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
@ApplicationScoped
public class LoadBookService implements LoadBookUseCase {

    @Inject
    ReadBookPort readBookPort;

    @Override
    public List<Book> loadAllBooks(BookFilter filter, int pageIndex, int pageSize) {
        return readBookPort.readAllBooks(filter, pageIndex, pageSize);
    }

    @Override
    public Book loadBookbyId(Long id) {
        return readBookPort.readBookById(id).orElseThrow(() -> new EntityNotFoundException("Book with id " + id + " not found"));
    }
}
