package de.thws.adapter.out.persistance.repository;

import de.thws.adapter.out.persistance.entities.BookJpaEntity;
import de.thws.adapter.out.persistance.mapper.BookMapper;
import de.thws.domain.exception.DuplicateEntityException;
import de.thws.domain.model.Book;
import de.thws.domain.model.BookFilter;
import de.thws.domain.port.out.DeleteBookPort;
import de.thws.domain.port.out.PersistBookPort;
import de.thws.domain.port.out.ReadBookPort;
import de.thws.domain.port.out.UpdateBookPort;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.hibernate.exception.ConstraintViolationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public List<Book> readAllBooks(BookFilter filter, int pageIndex, int pageSize) {
        StringBuilder query = new StringBuilder("1=1");
        Map<String, Object> params = new HashMap<>();
        if (filter.getTitle() != null) {
            query.append(" AND lower(title) LIKE :title");
            params.put("title", "%" + filter.getTitle() + "%");
        }
        if (filter.getAuthor() != null) {
            query.append(" AND lower(author) LIKE :author");
            params.put("author", "%" + filter.getAuthor() + "%");
        }
        if (filter.getIsbn() != null) {
            query.append(" AND lower(isbn) LIKE :isbn");
            params.put("isbn", "%" + filter.getIsbn() + "%");
        }
        if (filter.getPublisher() != null) {
            query.append(" AND lower(publisher) LIKE :publisher");
            params.put("publisher", "%" + filter.getPublisher() + "%");
        }
        if (filter.getGenre() != null) {
            query.append(" AND lower(genre) LIKE :genre");
            params.put("genre", "%" + filter.getGenre() + "%");
        }

        final var jpaBooks = find(query.toString(), params).page(Page.of(pageIndex-1, pageSize)).list();
        return bookMapper.toDomainModels(jpaBooks);
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
