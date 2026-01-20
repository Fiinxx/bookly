package de.thws.domain.service;

import de.thws.domain.port.in.DeleteUserUseCase;
import de.thws.domain.port.out.DeleteUserPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DeleteUserService implements DeleteUserUseCase {

    @Inject
    DeleteUserPort deleteUserPort;

    @Override
    public void deleteUserById(long id){ this.deleteUserPort.deleteUserById(id); }

}
