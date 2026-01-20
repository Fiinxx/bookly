package de.thws.domain.service;

import de.thws.domain.exception.EntityNotFoundException;
import de.thws.domain.model.User;
import de.thws.domain.port.out.ReadUserPort;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@QuarkusTest
public class LoadUserServiceTest {

    @Inject
    LoadUserService loadUserService;

    @InjectMock
    ReadUserPort readUserPort;

    @Test
    void loadUserById_should_return_user_when_found() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        when(readUserPort.readUserById(1L)).thenReturn(Optional.of(user));

        // Act
        User result = loadUserService.loadUserById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("testuser", result.getUsername());
        verify(readUserPort).readUserById(1L);
    }

    @Test
    void loadUserById_should_throw_exception_when_not_found() {
        // Arrange
        when(readUserPort.readUserById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                loadUserService.loadUserById(99L)
        );

        assertEquals("User with id 99 not found", exception.getMessage());
        verify(readUserPort).readUserById(99L);
    }

    @Test
    void loadUserByUsername_should_return_user_when_found() {
        User user = new User();
        user.setUsername("alice");

        when(readUserPort.readUserByUsername("alice")).thenReturn(Optional.of(user));

        User result = loadUserService.loadUserByUsername("alice");

        assertNotNull(result);
        assertEquals("alice", result.getUsername());
        verify(readUserPort).readUserByUsername("alice");
    }

    @Test
    void loadUserByUsername_should_throw_exception_when_not_found() {
        when(readUserPort.readUserByUsername(anyString())).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                loadUserService.loadUserByUsername("unknown")
        );

        assertEquals("User with name unknown not found", exception.getMessage());
        verify(readUserPort).readUserByUsername("unknown");
    }

}