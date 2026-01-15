package de.thws.domain.port.out;

import de.thws.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface ReadUserPort {
    Optional<User> readUserById(Long id);
    Optional<User> readUserByUsername(String username);
    List<User> readAllUsers();
}
