package de.thws.adapter.in.api.mapper;
import de.thws.adapter.in.api.dto.BookDTO;
import de.thws.domain.model.Book;

import java.util.List;
import java.util.stream.Collectors;

public class BookMapper
{
    public BookDTO mapToApiModel(Book book)
    {
        final var returnValue = new BookDTO();

        returnValue.setId(book.getId());
        returnValue.setIsbn(book.getIsbn());
        returnValue.setTitle(book.getTitle());
        returnValue.setAuthor(book.getAuthor());
        returnValue.setPagecount(book.getPagecount());
        returnValue.setPublisher(book.getPublisher());
        returnValue.setGenre(book.getGenre());
        returnValue.setPrice(book.getPrice());
        returnValue.setLanguage(book.getLanguage());
        returnValue.setDescription(book.getDescription());
        returnValue.setPublishingDate(book.getPublishingDate());

        return returnValue;
    }

    public List<BookDTO> mapToApiModels(List<Book> resultList )
    {
        return resultList.stream( ).map( this::mapToApiModel ).collect( Collectors.toList( ) );
    }

    public Book mapToDomainModel(BookDTO bookDTO)
    {
        final var returnValue = new Book();

        returnValue.setId(bookDTO.getId());
        returnValue.setIsbn(bookDTO.getIsbn());
        returnValue.setTitle(bookDTO.getTitle());
        returnValue.setAuthor(bookDTO.getAuthor());
        returnValue.setPagecount(bookDTO.getPagecount());
        returnValue.setPublisher(bookDTO.getPublisher());
        returnValue.setGenre(bookDTO.getGenre());
        returnValue.setPrice(bookDTO.getPrice());
        returnValue.setLanguage(bookDTO.getLanguage());
        returnValue.setDescription(bookDTO.getDescription());
        returnValue.setPublishingDate(bookDTO.getPublishingDate());

        return returnValue;
    }
}

