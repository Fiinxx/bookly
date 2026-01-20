package de.thws.domain.service;

import de.thws.domain.model.Book;
import de.thws.domain.port.out.FetchBookDetailsPort;
import de.thws.domain.port.out.PersistBookPort;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
public class CreateBookServiceTest {

    @Inject
    CreateBookService createBookService;

    @InjectMock
    PersistBookPort persistBookPort;

    @InjectMock
    FetchBookDetailsPort fetchBookDetailsPort;

    @Test
    void createBook_should_persist_directly_when_title_exists() {
        Book book = new Book();
        book.setIsbn("978-3-16-148410-0");
        book.setTitle("Existing Title");

        createBookService.createBook(book);

        // verify enrichment was skipped
        verify(fetchBookDetailsPort, never()).fetchDetails(any());
        // verify persistence was called
        verify(persistBookPort).persistBook(book);
    }

    @Test
    void createBook_should_enrich_and_persist_when_title_is_missing() {
        String isbn = "978-3-16-148410-0";
        Book inputBook = new Book();
        inputBook.setIsbn(isbn);

        Book externalBook = new Book();
        externalBook.setTitle("Fetched Title");
        externalBook.setAuthor("Fetched Author");
        externalBook.setPublisher("Fetched Publisher");

        when(fetchBookDetailsPort.fetchDetails(isbn)).thenReturn(Optional.of(externalBook));

        createBookService.createBook(inputBook);

        assertEquals("Fetched Title", inputBook.getTitle());
        assertEquals("Fetched Author", inputBook.getAuthor());
        assertEquals("Fetched Publisher", inputBook.getPublisher());

        verify(fetchBookDetailsPort).fetchDetails(isbn);
        verify(persistBookPort).persistBook(inputBook);
    }

    @Test
    void createBook_should_persist_unenriched_when_fetch_returns_empty() {
        String isbn = "000-0000000000";
        Book inputBook = new Book();
        inputBook.setIsbn(isbn);

        when(fetchBookDetailsPort.fetchDetails(isbn)).thenReturn(Optional.empty());

        createBookService.createBook(inputBook);

        assertNull(inputBook.getTitle());
        verify(fetchBookDetailsPort).fetchDetails(isbn);
        verify(persistBookPort).persistBook(inputBook);
    }

    @Test
    void bulkAddBooks_should_enrich_only_missing_titles_and_persist_all() {
        Book completeBook = new Book();
        completeBook.setIsbn("111-111");
        completeBook.setTitle("Complete Book");

        Book incompleteBook = new Book();
        incompleteBook.setIsbn("222-222");

        Book externalBook = new Book();
        externalBook.setTitle("Fetched Book");

        when(fetchBookDetailsPort.fetchDetails("222-222")).thenReturn(Optional.of(externalBook));

        List<Book> books = List.of(completeBook, incompleteBook);

        createBookService.bulkAddBooks(books);

        verify(fetchBookDetailsPort, never()).fetchDetails("111-111");
        verify(fetchBookDetailsPort).fetchDetails("222-222");

        assertEquals("Fetched Book", incompleteBook.getTitle());

        verify(persistBookPort).persistBooks(books);
    }
}