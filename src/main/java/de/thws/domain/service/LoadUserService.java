package de.thws.domain.service;

import de.thws.domain.model.User;
import de.thws.domain.port.in.LoadUserUseCase;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.Serializable;
import java.util.List;

@ApplicationScoped
public class LoadUserService implements LoadUserUseCase {
    @Override
    public List<User> loadAllUsers() {
        return List.of();
    }

    @Override
    public User loadUserById(Long id) {
        return null;
    }
}
