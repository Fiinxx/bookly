package de.thws.adapter.out.persistance.repository;

import de.thws.adapter.out.persistance.mapper.BookMapper;
import de.thws.domain.model.Book;
import de.thws.domain.port.out.DeleteBookPort;
import de.thws.domain.port.out.PersistBookPort;
import de.thws.domain.port.out.ReadBookPort;
import de.thws.domain.port.out.UpdateBookPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;
@ApplicationScoped
public class BookPersistenceAdapter implements PersistBookPort, DeleteBookPort, ReadBookPort, UpdateBookPort {

    private BookMapper bookMapper = new BookMapper();

    @Inject
    private EntityManager entityManager;


    @Override
    public void deleteBook(Book book) {

    }
    @Transactional
    @Override
    public void persistBook(Book book) {

            final var jpaBook = bookMapper.mapToJpaEntity(book);
            entityManager.persist(jpaBook);
            book.setId(jpaBook.getId());


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
