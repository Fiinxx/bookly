package de.thws.domain.service;

import de.thws.domain.exception.EntityNotFoundException;
import de.thws.domain.port.out.DeleteUserPort;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class DeleteUserServiceTest {

    @Inject
    DeleteUserService deleteUserService;

    @InjectMock
    DeleteUserPort deleteUserPort;

    @Test
    void deleteUserById_should_delegate_to_port_on_success() {
        long userId = 1L;
        Mockito.doNothing().when(deleteUserPort).deleteUserById(userId);
        assertDoesNotThrow(() -> deleteUserService.deleteUserById(userId));
        Mockito.verify(deleteUserPort, Mockito.times(1)).deleteUserById(userId);
    }

    @Test
    void deleteUserById_should_propagate_EntityNotFoundException_when_user_missing() {
        long nonExistingId = 999L;
        Mockito.doThrow(new EntityNotFoundException("User not found"))
                .when(deleteUserPort).deleteUserById(nonExistingId);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            deleteUserService.deleteUserById(nonExistingId);
        });

        assertEquals("User not found", exception.getMessage());
        Mockito.verify(deleteUserPort).deleteUserById(nonExistingId);
    }
}