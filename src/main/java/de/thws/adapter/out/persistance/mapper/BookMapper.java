package de.thws.adapter.out.persistance.mapper;

import de.thws.adapter.out.persistance.entities.BookJpaEntity;
import de.thws.domain.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "jakarta", implementationName = "PersistenceBookMapperImpl")
public interface BookMapper {
    @Mapping(target = "ratings", ignore = true)
    BookJpaEntity toJpaEntity(Book book);
    Book toDomainModel(BookJpaEntity bookJpaEntity);
    List<Book> toDomainModels(List<BookJpaEntity> bookJpaEntities);
}
