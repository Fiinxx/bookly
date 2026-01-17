package de.thws.adapter.out.persistance.mapper;

import de.thws.adapter.out.persistance.entities.BookJpaEntity;
import de.thws.domain.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "jakarta", implementationName = "PersistenceBookMapperImpl")
public interface BookMapper {
    @Mapping(target = "ratings", ignore = true)
    BookJpaEntity toJpaEntity(Book book);
    Book toDomainModel(BookJpaEntity bookJpaEntity);
    List<Book> toDomainModels(List<BookJpaEntity> bookJpaEntities);

    @Mapping(target = "id", ignore = true)           // ID never changes
    @Mapping(target = "ratings", ignore = true)         // You cannot change WHICH book is rated
    void updateJpaFromDomain(Book book, @MappingTarget BookJpaEntity entity);
}
