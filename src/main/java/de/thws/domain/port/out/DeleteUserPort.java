package de.thws.domain.port.out;

import de.thws.domain.model.User;

public interface DeleteUserPort {
    void deleteUser(User user);
}
