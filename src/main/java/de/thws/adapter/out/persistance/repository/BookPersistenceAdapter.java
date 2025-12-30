package de.thws.adapter.out.persistance.repository;

import de.thws.domain.model.Book;
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
    public List<Book> readAllBooks() {
        return List.of();
    }

    @Override
    public Book readBookById(String id) {
        return null;
    }

    @Override
    public void updateBook(Book book) {

    }
}
