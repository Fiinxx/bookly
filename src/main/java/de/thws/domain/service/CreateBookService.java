package de.thws.domain.service;

import de.thws.domain.model.Book;
import de.thws.domain.port.in.CreateBookUseCase;
import de.thws.domain.port.out.FetchBookDetailsPort;
import de.thws.domain.port.out.PersistBookPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class CreateBookService  implements CreateBookUseCase {

    @Inject
    PersistBookPort persistBookPort;

    @Inject
    FetchBookDetailsPort fetchBookDetailsPort;

    @Override
    public void createBook(Book book) {
        //check if book needs enrichment
        if (book.getTitle() == null || book.getTitle().isBlank()) {
            final var externalBook = fetchBookDetailsPort.fetchDetails(book.getIsbn());

            if (externalBook.isPresent()) {
                Book ext = externalBook.get();
                book.setTitle(book.getTitle() != null ? book.getTitle() : ext.getTitle());
                book.setAuthor(book.getAuthor() != null ? book.getAuthor() : ext.getAuthor());
                book.setPublisher(book.getPublisher() != null ? book.getPublisher() : ext.getPublisher());
                book.setDescription(book.getDescription() != null ? book.getDescription() : ext.getDescription());
                book.setPublishingDate(book.getPublishingDate() != null ? book.getPublishingDate() : ext.getPublishingDate());
                book.setLanguage(book.getLanguage() != null ? book.getLanguage() : ext.getLanguage());
            }
        }
        this.persistBookPort.persistBook(book);
    }

    @Override
    public void bulkAddBooks(List<Book> books) {

    }


}
