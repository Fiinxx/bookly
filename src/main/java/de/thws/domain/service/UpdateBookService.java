package de.thws.domain.service;

import de.thws.domain.model.Book;
import de.thws.domain.port.in.UpdateBookUseCase;
import de.thws.domain.port.out.UpdateBookPort;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UpdateBookService implements UpdateBookUseCase {

    @Inject
    UpdateBookPort updateBookPort;

    @Override
    public Book updateBook(Book book) {
        return this.updateBookPort.updateBook(book);
    }

}
