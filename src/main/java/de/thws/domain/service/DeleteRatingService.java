package de.thws.domain.service;

import de.thws.domain.port.in.DeleteRatingUseCase;
import de.thws.domain.port.out.DeleteRatingPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DeleteRatingService implements DeleteRatingUseCase {
    @Inject
    DeleteRatingPort deleteRatingPort;

    @Override
    public boolean deleteRatingById(long id){
        return deleteRatingPort.deleteRatingById(id);
    }
}
