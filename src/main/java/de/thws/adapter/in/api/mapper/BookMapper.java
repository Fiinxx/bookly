package de.thws.adapter.in.api.mapper;
import de.thws.adapter.in.api.dto.BookDtos;
import de.thws.adapter.in.api.dto.BookFilterDto;
import de.thws.domain.model.Book;
import de.thws.domain.model.BookFilter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "jakarta", implementationName = "ApiBookMapperImpl")
public interface BookMapper
{
    @Mapping(target = "id", ignore = true)
    Book toDomain(BookDtos.Create request);
    List<Book> toDomains(List<BookDtos.Create> request);
    BookDtos.Detail toDetail(Book book);
    List<BookDtos.Detail> toDetails(List<Book> books);
    BookFilter toDomain(BookFilterDto bookFilterDto);
    Book toDomain(BookDtos.Detail request);
}

