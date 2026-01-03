package de.thws.adapter.in.api.mapper;
import de.thws.adapter.in.api.dto.BookDtos;
import de.thws.domain.model.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "jakarta")
public interface BookMapper
{
    Book toDomain(BookDtos.Create request);
    BookDtos.Detail toDetail(Book book);

}

