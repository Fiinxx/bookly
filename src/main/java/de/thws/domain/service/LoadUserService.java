package de.thws.domain.service;

import de.thws.domain.exception.EntityNotFoundException;
import de.thws.domain.model.User;
import de.thws.domain.port.in.LoadUserUseCase;
import de.thws.domain.port.out.ReadUserPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class LoadUserService implements LoadUserUseCase {

    @Inject
    ReadUserPort readUserPort;

    @Override
    public List<User> loadAllUsers() {
        return List.of();
    }

    @Override
    public User loadUserById(Long id) {
        return readUserPort.readUserById(id).orElseThrow(() ->  new EntityNotFoundException("User with id " + id + " not found"));
    }

    @Override
    public User loadUserByUsername(String username) {
        return readUserPort.readUserByUsername(username).orElseThrow(() ->  new EntityNotFoundException("User with name " + username + " not found"));
    }
}
