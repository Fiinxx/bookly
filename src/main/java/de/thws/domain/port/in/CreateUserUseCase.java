package de.thws.domain.port.in;

import de.thws.domain.model.User;

public interface CreateUserUseCase {
    void createUser(User user);
}
