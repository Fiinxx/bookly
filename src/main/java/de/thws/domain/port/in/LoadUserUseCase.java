package de.thws.domain.port.in;

import de.thws.domain.model.User;

import java.util.List;

public interface LoadUserUseCase {

    List<User> loadAllUsers();
    User loadUserById(Long id);
}
