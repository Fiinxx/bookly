package de.thws.adapter.out.persistance.repository;


import de.thws.adapter.out.persistance.entities.UserJpaEntity;
import de.thws.adapter.out.persistance.mapper.UserMapper;
import de.thws.domain.exception.DuplicateEntityException;
import de.thws.domain.exception.EntityNotFoundException;
import de.thws.domain.model.User;
import de.thws.domain.port.out.DeleteUserPort;
import de.thws.domain.port.out.PersistUserPort;
import de.thws.domain.port.out.ReadUserPort;
import de.thws.domain.port.out.UpdateUserPort;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserPersistenceAdapter implements PanacheRepository<UserJpaEntity>, DeleteUserPort, PersistUserPort, ReadUserPort,UpdateUserPort  {

    @Inject
    UserMapper userMapper;


    @Transactional
    @Override
    public void persistUser(User user) {
        try{
            UserJpaEntity userJpaEntity = userMapper.toJpaEntity(user);
            persist(userJpaEntity);
            user.setId(userJpaEntity.getId());
        }catch (ConstraintViolationException e){
            throw new DuplicateEntityException("User with this username or email already exists");
        }
    }

    @Override
    public Optional<User> readUserById(Long id) {
        final var jpaUser = findById(id);
        return Optional.ofNullable(jpaUser).
                map(userMapper::toDomainModel);
    }

    @Override
    public Optional<User> readUserByUsername(String username) {
        final var jpaUser = find("username", username).firstResult();
        return Optional.ofNullable(jpaUser).
                map(userMapper::toDomainModel);
    }

    @Override
    public List<User> readAllUsers() {
        return List.of();
    }

    @Transactional
    @Override
    public User updateUser(User user) {
        try {
            UserJpaEntity entity = findById(user.getId());
            if (entity == null) {
                throw new EntityNotFoundException("User not found");
            }
            userMapper.updateJpaFromDomain(user, entity);
            flush();
            return userMapper.toDomainModel(entity);
        } catch (ConstraintViolationException e) {
            throw new DuplicateEntityException("Username or Email already exists");
        }
    }

    @Override
    @Transactional
    public void deleteUserById(long id){
            UserJpaEntity entity = findById(id);
        if (entity == null) {
            throw new EntityNotFoundException("User not found");
        }
        delete(entity);
    }
}
