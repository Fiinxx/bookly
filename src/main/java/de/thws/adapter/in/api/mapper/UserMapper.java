package de.thws.adapter.in.api.mapper;

import de.thws.adapter.in.api.dto.UserDtos;
import de.thws.domain.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "jakarta")
public interface UserMapper
{
    UserDtos.Detail toDetail(User user);
    List<UserDtos.Detail> toDetails(List<User> users);
}


