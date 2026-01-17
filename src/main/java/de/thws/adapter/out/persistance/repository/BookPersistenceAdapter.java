package de.thws.adapter.out.persistance.repository;

import de.thws.adapter.out.persistance.entities.BookJpaEntity;
import de.thws.adapter.out.persistance.mapper.BookMapper;
import de.thws.domain.exception.DuplicateEntityException;
import de.thws.domain.exception.EntityNotFoundException;
import de.thws.domain.model.Book;
import de.thws.domain.model.BookFilter;
import de.thws.domain.port.out.DeleteBookPort;
import de.thws.domain.port.out.PersistBookPort;
import de.thws.domain.port.out.ReadBookPort;
import de.thws.domain.port.out.UpdateBookPort;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
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


    @Transactional
    @Override
    public void persistBook(Book book) {
        try {
            final var jpaBook = bookMapper.toJpaEntity(book);
            persist(jpaBook);
            flush();
            book.setId(jpaBook.getId());
        } catch (ConstraintViolationException e) {
            throw new DuplicateEntityException("Book with isbn " + book.getIsbn() + " already exists");
        }
    }

    @Transactional
    @Override
    public void persistBooks(List<Book> books) {
        try {
            for (Book book : books) {
                final var jpaBook = bookMapper.toJpaEntity(book);
                persist(jpaBook);
                book.setId(jpaBook.getId());
            }
            flush();
        }catch (ConstraintViolationException e) {
            throw new DuplicateEntityException("Batch failed: One or more books already exist.");
        }
    }

    @Override
    public List<Book> readAllBooks(BookFilter filter, int pageIndex, int pageSize) {
        StringBuilder query = new StringBuilder("1=1");
        Map<String, Object> params = new HashMap<>();
        if (filter.getTitle() != null && !filter.getTitle().isBlank()) {
            query.append(" AND lower(title) LIKE :title");
            params.put("title", "%" + filter.getTitle().toLowerCase() + "%");
        }
        if (filter.getAuthor() != null && !filter.getAuthor().isBlank()) {
            query.append(" AND lower(author) LIKE :author");
            params.put("author", "%" + filter.getAuthor().toLowerCase() + "%");
        }
        if (filter.getIsbn() != null && !filter.getIsbn().isBlank()) {
            query.append(" AND lower(isbn) LIKE :isbn");
            params.put("isbn", "%" + filter.getIsbn() + "%");
        }
        if (filter.getPublisher() != null && !filter.getPublisher().isBlank()) {
            query.append(" AND lower(publisher) LIKE :publisher");
            params.put("publisher", "%" + filter.getPublisher().toLowerCase() + "%");
        }
        if (filter.getGenre() != null && !filter.getGenre().isBlank()) {
            query.append(" AND lower(genre) LIKE :genre");
            params.put("genre", "%" + filter.getGenre().toLowerCase() + "%");
        }

        int startIndex = (pageIndex - 1) * pageSize;
        int endIndex = startIndex + pageSize;
        final var jpaBooks = find(query.toString(), params).range(startIndex, endIndex).list();
        return bookMapper.toDomainModels(jpaBooks);
    }

    @Override
    public Optional<Book> readBookById(Long id) {
        final var jpaBook = findById(id);
        return Optional.ofNullable(jpaBook).
                map(bookMapper::toDomainModel);
    }

    @Transactional
    @Override
    public Book updateBook(Book book) {
        try {
            BookJpaEntity entity = findById(book.getId());
            if (entity == null) {
                throw new EntityNotFoundException("Book not found");
            }
            bookMapper.updateJpaFromDomain(book, entity);
            flush();
            return bookMapper.toDomainModel(entity);
        } catch (ConstraintViolationException e) {
            throw new DuplicateEntityException("Book with isbn " + book.getIsbn() + " already exists");
        }
    }
    @Override
    @Transactional
    public boolean deleteBookById(long id){
        BookJpaEntity entity = findById(id);
        if (entity == null) {
            return false;
        }
        delete(entity);
        return true;
    }
}
