package de.thws.adapter.out.persistance.repository;

import de.thws.adapter.out.persistance.entities.BookJpaEntity;
import de.thws.adapter.out.persistance.mapper.BookMapper;
import de.thws.domain.exception.DuplicateEntityException;
import de.thws.domain.model.Book;
import de.thws.domain.port.out.DeleteBookPort;
import de.thws.domain.port.out.PersistBookPort;
import de.thws.domain.port.out.ReadBookPort;
import de.thws.domain.port.out.UpdateBookPort;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class BookPersistenceAdapter implements PanacheRepository<BookJpaEntity>, PersistBookPort, DeleteBookPort, ReadBookPort, UpdateBookPort {

    @Inject
    BookMapper bookMapper;

    @Inject
    private EntityManager entityManager;


    @Override
    public void deleteBook(Book book) {

    }
    @Transactional
    @Override
    public void persistBook(Book book) {
        try {
            final var jpaBook = bookMapper.toJpaEntity(book);
            entityManager.persist(jpaBook);
            entityManager.flush();
            book.setId(jpaBook.getId());
        } catch (ConstraintViolationException e) {
            throw new DuplicateEntityException("Book with isbn " + book.getIsbn() + " already exists");
        }
    }

    @Override
    public List<Book> readAllBooks() {
        return List.of();
    }

    @Override
    public Optional<Book> readBookById(Long id) {
        final var jpaBook = entityManager.find(BookJpaEntity.class, id);
        return Optional.ofNullable(jpaBook).
                map(bookMapper::toDomainModel);
    }

    @Override
    public void updateBook(Book book) {

    }
}
