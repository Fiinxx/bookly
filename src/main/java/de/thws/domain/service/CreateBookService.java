package de.thws.domain.service;

import de.thws.domain.model.Book;
import de.thws.domain.port.in.CreateBookUseCase;
import de.thws.domain.port.out.PersistBookPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
@ApplicationScoped
public class CreateBookService  implements CreateBookUseCase {

    @Inject
    PersistBookPort persistBookPort;

    @Override
    public void createBook(Book book) {
        this.persistBookPort.persistBook(book);
    }

    @Override
    public void bulkAddBooks(List<Book> books) {

    }

    @Override
    public void addByISBN(String isbn) {

    }
}
