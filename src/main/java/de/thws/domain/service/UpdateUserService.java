package de.thws.domain.service;

import de.thws.domain.model.User;
import de.thws.domain.port.in.UpdateUserUseCase;
import de.thws.domain.port.out.UpdateUserPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UpdateUserService implements UpdateUserUseCase {

    @Inject
    private UpdateUserPort updateUserPort;

    @Override
    public void updateUser(User user) {
        this.updateUserPort.updateUser(user);
    }
}
