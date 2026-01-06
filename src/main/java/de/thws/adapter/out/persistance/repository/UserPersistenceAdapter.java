package de.thws.adapter.out.persistance.repository;


import de.thws.adapter.out.persistance.entities.UserJpaEntity;
import de.thws.adapter.out.persistance.mapper.RatingMapper;
import de.thws.adapter.out.persistance.mapper.UserMapper;
import de.thws.domain.model.User;
import de.thws.domain.port.out.DeleteUserPort;
import de.thws.domain.port.out.PersistUserPort;
import de.thws.domain.port.out.ReadUserPort;
import de.thws.domain.port.out.UpdateUserPort;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserPersistenceAdapter implements PanacheRepository<UserJpaEntity>, DeleteUserPort, PersistUserPort, ReadUserPort,UpdateUserPort  {

    @Inject
    UserMapper userMapper;

    @Inject
    private EntityManager entityManager;

    @Override
    public void deleteUser(User user) {

    }

    @Override
    public void persistUser(User user) {

    }

    @Override
    public Optional<User> readUserById(Long id) {
        final var jpaUser = entityManager.find(UserJpaEntity.class, id);
        return Optional.ofNullable(jpaUser).
                map(userMapper::toDomainModel);
    }

    @Override
    public List<User> readAllUsers() {
        return List.of();
    }

    @Override
    public void updateUser(User user) {

    }
}
