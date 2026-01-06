package de.thws.adapter.out.persistance.mapper;

import de.thws.adapter.out.persistance.entities.UserJpaEntity;
import de.thws.domain.model.User;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "jakarta", implementationName = "PersistenceUserMapperImpl")
public interface UserMapper {
    UserJpaEntity toJpaEntity(User user);
    List<User> toDomainModels(List<UserJpaEntity> resultList );
    User toDomainModel (UserJpaEntity jpaEntity);
}
