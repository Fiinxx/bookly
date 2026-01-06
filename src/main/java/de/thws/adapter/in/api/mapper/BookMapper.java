package de.thws.adapter.in.api.mapper;
import de.thws.adapter.in.api.dto.BookDtos;
import de.thws.domain.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "jakarta", implementationName = "ApiBookMapperImpl")
public interface BookMapper
{
    @Mapping(target = "id", ignore = true)
    Book toDomain(BookDtos.Create request);
    BookDtos.Detail toDetail(Book book);

}

