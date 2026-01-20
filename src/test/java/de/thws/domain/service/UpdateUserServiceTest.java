package de.thws.domain.service;

import de.thws.domain.exception.DuplicateEntityException;
import de.thws.domain.exception.EntityNotFoundException;
import de.thws.domain.model.User;
import de.thws.domain.port.out.UpdateUserPort;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
public class UpdateUserServiceTest {

    @Inject
    UpdateUserService updateUserService;

    @InjectMock
    UpdateUserPort updateUserPort;

    @Test
    void updateUser_should_return_updated_user_when_update_is_successful() {
        User inputUser = new User();
        inputUser.setId(1L);
        inputUser.setUsername("newUsername");

        User returnedUser = new User();
        returnedUser.setId(1L);
        returnedUser.setUsername("newUsername");

        when(updateUserPort.updateUser(inputUser)).thenReturn(returnedUser);

        User result = updateUserService.updateUser(inputUser);

        assertEquals(returnedUser, result);
        verify(updateUserPort).updateUser(inputUser);
    }

    @Test
    void updateUser_should_throw_EntityNotFoundException_when_user_id_does_not_exist() {
        User inputUser = new User();
        inputUser.setId(999L); // Non-existent ID

        when(updateUserPort.updateUser(inputUser))
                .thenThrow(new EntityNotFoundException("User not found"));

        assertThrows(EntityNotFoundException.class, () ->
                updateUserService.updateUser(inputUser)
        );
        verify(updateUserPort).updateUser(inputUser);
    }

    @Test
    void updateUser_should_throw_DuplicateEntityException_when_username_already_taken() {
        User inputUser = new User();
        inputUser.setId(1L);
        inputUser.setUsername("existingUser");

        when(updateUserPort.updateUser(inputUser))
                .thenThrow(new DuplicateEntityException("Username or Email already exists"));

        assertThrows(DuplicateEntityException.class, () ->
                updateUserService.updateUser(inputUser)
        );
        verify(updateUserPort).updateUser(inputUser);
    }
}