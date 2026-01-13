package de.thws.adapter.in.api.mapper;

import de.thws.adapter.in.api.dto.UserDtos;
import de.thws.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "jakarta", implementationName = "ApiUserMapperImpl")
public interface UserMapper
{
    UserDtos.Detail toDetail(User user);
    List<UserDtos.Detail> toDetails(List<User> users);
    @Mapping(target = "id", ignore = true)
    User toDomain(UserDtos.Create request);
}


