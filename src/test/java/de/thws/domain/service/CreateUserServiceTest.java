package de.thws.domain.service;

import de.thws.domain.exception.DuplicateEntityException;
import de.thws.domain.model.Role;
import de.thws.domain.model.User;
import de.thws.domain.port.out.PersistUserPort;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@QuarkusTest
class CreateUserServiceTest {

    @Inject
    CreateUserService createUserService;

    @InjectMock
    PersistUserPort persistUserPort;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@bookly.com");
        testUser.setPassword("plainPassword123");
        testUser.setRole(Role.USER);
    }

    @Test
    void createUser_should_hash_password_before_persisting() {
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        String originalPassword = testUser.getPassword();

        createUserService.createUser(testUser);

        verify(persistUserPort).persistUser(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        // Prüfen, ob Passwort nicht mehr im Klartext vorliegt
        assertNotEquals(originalPassword, savedUser.getPassword(), "Das Passwort sollte verschlüsselt sein.");

        // Verifizieren, dass es ein gültiger Bcrypt-Hash ist
        assertTrue(BcryptUtil.matches(originalPassword, savedUser.getPassword()),
                "Das verschlüsselte Passwort sollte mit dem Original übereinstimmen (Bcrypt match).");
    }

    @Test
    void createUser_should_delegate_to_persistUserPort() {
        createUserService.createUser(testUser);

        // Sicherstellen, dass Persistenz-Schicht aufgerufen wurde
        verify(persistUserPort).persistUser(testUser);
    }

    @Test
    void createUser_should_propagate_DuplicateEntityException() {
        // Simulieren, dass Username oder E-Mail bereits existieren
        Mockito.doThrow(new DuplicateEntityException("User with this username or email already exists"))
                .when(persistUserPort).persistUser(any(User.class));

        assertThrows(DuplicateEntityException.class, () -> {
            createUserService.createUser(testUser);
        }, "Der Service sollte die DuplicateEntityException bei Namens- oder E-Mail-Konflikten weitergeben.");
    }

    @Test
    void createUser_with_null_password_should_throw_exception() {
        testUser.setPassword(null);

        // Sicherstellen, dass Service nicht einfach weitermacht
        assertThrows(Exception.class, () -> {
            createUserService.createUser(testUser);
        }, "Der Service sollte eine Exception werfen, wenn das Passwort null ist.");
    }

    @Test
    void createUser_should_preserve_assigned_role() {
        testUser.setRole(Role.ADMIN);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        createUserService.createUser(testUser);

        verify(persistUserPort).persistUser(userCaptor.capture());
        assertEquals(Role.ADMIN, userCaptor.getValue().getRole(),
                "Die vom Controller zugewiesene Rolle muss erhalten bleiben.");
    }
}