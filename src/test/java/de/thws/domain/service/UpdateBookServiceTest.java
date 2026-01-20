package de.thws.domain.service;

import de.thws.domain.exception.DuplicateEntityException;
import de.thws.domain.exception.EntityNotFoundException;
import de.thws.domain.model.Book;
import de.thws.domain.port.out.UpdateBookPort;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
public class UpdateBookServiceTest {

    @Inject
    UpdateBookService updateBookService;

    @InjectMock
    UpdateBookPort updateBookPort;

    @Test
    void updateBook_should_return_updated_book_when_update_is_successful() {
        Book inputBook = new Book();
        inputBook.setId(1L);
        inputBook.setTitle("Updated Title");
        inputBook.setIsbn("978-3-16-148410-0");

        Book returnedBook = new Book();
        returnedBook.setId(1L);
        returnedBook.setTitle("Updated Title");
        returnedBook.setIsbn("978-3-16-148410-0");

        when(updateBookPort.updateBook(inputBook)).thenReturn(returnedBook);

        Book result = updateBookService.updateBook(inputBook);

        assertEquals(returnedBook, result);
        verify(updateBookPort).updateBook(inputBook);
    }

    @Test
    void updateBook_should_throw_EntityNotFoundException_when_book_does_not_exist() {
        Book inputBook = new Book();
        inputBook.setId(999L);
        inputBook.setTitle("Missing Book");

        when(updateBookPort.updateBook(inputBook))
                .thenThrow(new EntityNotFoundException("Book not found"));

        assertThrows(EntityNotFoundException.class, () ->
                updateBookService.updateBook(inputBook)
        );
        verify(updateBookPort).updateBook(inputBook);
    }

    @Test
    void updateBook_should_throw_DuplicateEntityException_when_isbn_already_exists() {
        Book inputBook = new Book();
        inputBook.setId(1L);
        inputBook.setIsbn("978-3-16-148410-0"); // ISBN belonging to another book

        when(updateBookPort.updateBook(inputBook))
                .thenThrow(new DuplicateEntityException("Book with isbn " + inputBook.getIsbn() + " already exists"));

        assertThrows(DuplicateEntityException.class, () ->
                updateBookService.updateBook(inputBook)
        );
        verify(updateBookPort).updateBook(inputBook);
    }
}