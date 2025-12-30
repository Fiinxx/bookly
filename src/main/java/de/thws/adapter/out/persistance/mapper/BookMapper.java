package de.thws.adapter.out.persistance.mapper;

import de.thws.adapter.out.persistance.entities.BookJpaEntity;
import de.thws.domain.model.Book;

import java.util.List;
import java.util.stream.Collectors;

public class BookMapper {

    public BookJpaEntity mapToJpaEntity(Book book)
    {
        final var returnValue = new BookJpaEntity();

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

    public List<Book> mapToDomainModels(List<BookJpaEntity> resultList )
    {
        return resultList.stream( ).map( this::mapToDomainModel ).collect( Collectors.toList( ) );
    }

    public Book mapToDomainModel(BookJpaEntity jpaEntity)
    {
        final var returnValue = new Book();

        returnValue.setId(jpaEntity.getId());
        returnValue.setIsbn(jpaEntity.getIsbn());
        returnValue.setTitle(jpaEntity.getTitle());
        returnValue.setAuthor(jpaEntity.getAuthor());
        returnValue.setPagecount(jpaEntity.getPagecount());
        returnValue.setPublisher(jpaEntity.getPublisher());
        returnValue.setGenre(jpaEntity.getGenre());
        returnValue.setPrice(jpaEntity.getPrice());
        returnValue.setLanguage(jpaEntity.getLanguage());
        returnValue.setDescription(jpaEntity.getDescription());
        returnValue.setPublishingDate(jpaEntity.getPublishingDate());

        return returnValue;
    }
}
