package de.thws.domain.service;

import de.thws.domain.model.User;
import de.thws.domain.port.in.CreateUserUseCase;
import de.thws.domain.port.out.PersistUserPort;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CreateUserService implements CreateUserUseCase {
    @Inject
    PersistUserPort persistUserPort;

    @Override
    public void createUser(User user) {
        user.setPassword(BcryptUtil.bcryptHash(user.getPassword()));
        persistUserPort.persistUser(user);
    }
}
