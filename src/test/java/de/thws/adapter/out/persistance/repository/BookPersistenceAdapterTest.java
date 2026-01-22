package de.thws.adapter.out.persistance.repository;

import de.thws.domain.exception.DuplicateEntityException;
import de.thws.domain.exception.EntityNotFoundException;
import de.thws.domain.model.Book;
import de.thws.domain.model.BookFilter;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(2)
class BookPersistenceAdapterTest {

    @Inject
    BookPersistenceAdapter bookPersistenceAdapter;

    @Test
    @TestTransaction
    void persistBook_should_save_book_and_assign_id() {
        Book book = createSampleBook("111-222-333");

        bookPersistenceAdapter.persistBook(book);

        assertNotNull(book.getId());
        Optional<Book> loaded = bookPersistenceAdapter.readBookById(book.getId());
        assertTrue(loaded.isPresent());
        assertEquals("111-222-333", loaded.get().getIsbn());
    }

    @Test
    @TestTransaction
    void persistBook_should_throw_DuplicateEntityException_when_isbn_exists() {
        String isbn = "978-0132350884";
        Book duplicateBook = createSampleBook(isbn);


        assertThrows(DuplicateEntityException.class, () -> {
            bookPersistenceAdapter.persistBook(duplicateBook);
        });
    }


    @Test
    void readAllBooks_filter_by_title_ignore_case() {
        BookFilter filter = new BookFilter();
        filter.setTitle("clean code");

        List<Book> result = bookPersistenceAdapter.readAllBooks(filter, 1, 10);

        assertFalse(result.isEmpty());
        assertTrue(result.stream().anyMatch(b -> b.getTitle().equals("Clean Code")));
    }

    @Test
    void readAllBooks_filter_by_author_should_find_martin() {
        BookFilter filter = new BookFilter();
        filter.setAuthor("Martin");

        List<Book> result = bookPersistenceAdapter.readAllBooks(filter, 1, 10);

        assertEquals(1, result.size());
        assertEquals("Robert C. Martin", result.get(0).getAuthor());
    }


    @Test
    @TestTransaction
    void updateBook_should_change_details_in_db() {
        Book book = bookPersistenceAdapter.readBookById(1L).get();
        book.setTitle("Updated Title");

        bookPersistenceAdapter.updateBook(book);

        Book updated = bookPersistenceAdapter.readBookById(1L).get();
        assertEquals("Updated Title", updated.getTitle());
    }

    @Test
    @TestTransaction
    void updateBook_should_throw_NotFound_when_id_invalid() {
        Book book = createSampleBook("999");
        book.setId(9999L);

        assertThrows(EntityNotFoundException.class, () -> {
            bookPersistenceAdapter.updateBook(book);
        });
    }


    @Test
    @TestTransaction
    void deleteBookById_should_remove_entry() {
        bookPersistenceAdapter.deleteBookById(4L);

        Optional<Book> deleted = bookPersistenceAdapter.readBookById(4L);
        assertFalse(deleted.isPresent());
    }

    @Test
    void deleteBookById_should_throw_NotFound_if_missing() {
        assertThrows(EntityNotFoundException.class, () -> {
            bookPersistenceAdapter.deleteBookById(9999L);
        });
    }

    private Book createSampleBook(String isbn) {
        Book book = new Book();
        book.setIsbn(isbn);
        book.setTitle("Persistence Test");
        book.setAuthor("Test Author");
        return book;
    }
}