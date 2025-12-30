package de.thws.adapter.out.persistance.mapper;

import de.thws.adapter.out.persistance.entities.UserJpaEntity;
import de.thws.domain.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public UserJpaEntity mapToJpaEntity(User user)
    {
        final var returnValue = new UserJpaEntity();

        returnValue.setId(user.getId());
        returnValue.setUsername(user.getUsername());
        returnValue.setEmail(user.getEmail());
        returnValue.setPassword(user.getPassword());
        returnValue.setRole(user.getRole());

        return returnValue;
    }

    public List<User> mapToDomainModels(List<UserJpaEntity> resultList )
    {
        return resultList.stream( ).map( this::mapToDomainModel ).collect( Collectors.toList( ) );
    }

    public User mapToDomainModel (UserJpaEntity jpaEntity)
    {
        final var returnValue = new User();

        returnValue.setId(jpaEntity.getId());
        returnValue.setUsername(jpaEntity.getUsername());
        returnValue.setEmail(jpaEntity.getEmail());
        returnValue.setPassword(jpaEntity.getPassword());
        returnValue.setRole(jpaEntity.getRole());

        return returnValue;
    }
}
