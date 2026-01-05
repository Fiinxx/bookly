package de.thws.adapter.out.persistance.repository;

import de.thws.domain.model.User;
import de.thws.domain.port.out.DeleteUserPort;
import de.thws.domain.port.out.PersistUserPort;
import de.thws.domain.port.out.ReadUserPort;
import de.thws.domain.port.out.UpdateUserPort;

import java.util.List;
import java.util.Optional;

public class UserPersistenceAdapter implements DeleteUserPort, PersistUserPort, ReadUserPort,UpdateUserPort  {

    @Override
    public void deleteUser(User user) {

    }

    @Override
    public void persistUser(User user) {

    }

    @Override
    public Optional<User> readUserById(Long id) {
        return null;
    }

    @Override
    public List<User> readAllUsers() {
        return List.of();
    }

    @Override
    public void updateUser(User user) {

    }
}
