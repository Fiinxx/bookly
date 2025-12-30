package de.thws.adapter.in.api.mapper;

import de.thws.adapter.in.api.dto.UserDTO;
import de.thws.domain.model.Role;
import de.thws.domain.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper
{
    public UserDTO mapToApiModel(User user)
    {
        final var returnValue = new UserDTO();

        returnValue.setId(user.getId());
        returnValue.setUsername(user.getUsername());
        returnValue.setEmail(user.getEmail());
        returnValue.setPassword(user.getPassword());
        returnValue.setRole(String.valueOf(user.getRole()));

        return returnValue;
    }

    public List<UserDTO> mapToApiModels(List<User> resultList )
    {
        return resultList.stream( ).map( this::mapToApiModel ).collect( Collectors.toList( ) );
    }

    public User mapToDomainModel (UserDTO userDTO)
    {
        final var returnValue = new User();

        returnValue.setId(userDTO.getId());
        returnValue.setUsername(userDTO.getUsername());
        returnValue.setEmail(userDTO.getEmail());
        returnValue.setPassword(userDTO.getPassword());
        returnValue.setRole(Enum.valueOf(Role.class, userDTO.getRole()));

        return returnValue;
    }
}


