package de.thws.domain.port.out;

import de.thws.domain.model.User;

import java.util.List;

public interface ReadUserPort {
    User readUserById(int id);
    List<User> readAllUsers();
}
