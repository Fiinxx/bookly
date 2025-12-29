package de.thws.domain.port.in;

import de.thws.domain.model.User;

public interface DeleteUserUseCase {
    void deleteUser(User user);
}
