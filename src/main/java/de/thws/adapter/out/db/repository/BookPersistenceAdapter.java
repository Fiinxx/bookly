package de.thws.adapter.out.db.repository;

import de.thws.domain.model.Book;
import de.thws.domain.port.in.CreateBookUseCase;
import de.thws.domain.port.in.DeleteBookUseCase;
import de.thws.domain.port.in.LoadBookUseCase;
import de.thws.domain.port.in.UpdateBookUseCase;
import de.thws.domain.port.out.DeleteBookPort;
import de.thws.domain.port.out.PersistBookPort;
import de.thws.domain.port.out.ReadBookPort;
import de.thws.domain.port.out.UpdateBookPort;

import java.util.List;

public class BookPersistenceAdapter implements PersistBookPort, DeleteBookPort, ReadBookPort, UpdateBookPort {
    @Override
    public void deleteBook(Book book) {

    }

    @Override
    public void persistBook(Book book) {

    }

    @Override
    public Book readAllBooks() {
        return null;
    }

    @Override
    public List<Book> readAllBooks(String id) {
        return List.of();
    }

    @Override
    public void updateBook(Book book) {

    }
}
