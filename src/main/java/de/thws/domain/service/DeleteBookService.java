package de.thws.domain.service;

import de.thws.domain.port.in.DeleteBookUseCase;
import de.thws.domain.port.out.DeleteBookPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DeleteBookService implements DeleteBookUseCase {

    @Inject
    DeleteBookPort deleteBookPort;

    @Override
    public void deleteBookById(long id){
        this.deleteBookPort.deleteBookById(id);
    }
}
