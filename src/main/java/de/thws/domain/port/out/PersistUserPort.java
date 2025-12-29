package de.thws.domain.port.out;

import de.thws.domain.model.User;

public interface PersistUserPort {
    void persistUser(User user);
}
