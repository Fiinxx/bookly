package de.thws.adapter.out.persistance.mapper;

import de.thws.adapter.out.persistance.entities.UserJpaEntity;
import de.thws.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "jakarta", implementationName = "PersistenceUserMapperImpl")
public interface UserMapper {
    @Mapping(target = "ratings", ignore = true)
    UserJpaEntity toJpaEntity(User user);
    List<User> toDomainModels(List<UserJpaEntity> resultList );
    User toDomainModel (UserJpaEntity jpaEntity);

    @Mapping(target = "id", ignore = true)       // ID never changes
    @Mapping(target = "ratings", ignore = true)  // Don't touch ratings
    @Mapping(target = "role", ignore = true)     // Don't touch role
    @Mapping(target = "password", ignore = true) // Don't touch password
    void updateJpaFromDomain(User user, @MappingTarget UserJpaEntity entity);
}
