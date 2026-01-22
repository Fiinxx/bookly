package de.thws.adapter.out.persistance.repository;

import de.thws.domain.exception.DuplicateEntityException;
import de.thws.domain.exception.EntityNotFoundException;
import de.thws.domain.model.Role;
import de.thws.domain.model.User;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(2)
class UserPersistenceAdapterTest {

    @Inject
    UserPersistenceAdapter userPersistenceAdapter;


    @Test
    @TestTransaction
    void persistUser_should_save_user_and_assign_id() {
        User user = new User();
        user.setUsername("test-user");
        user.setEmail("test@bookly.de");
        user.setPassword("secret");
        user.setRole(Role.USER);

        userPersistenceAdapter.persistUser(user);

        assertNotNull(user.getId());
        Optional<User> loaded = userPersistenceAdapter.readUserById(user.getId());
        assertTrue(loaded.isPresent());
        assertEquals("test-user", loaded.get().getUsername());
    }

    @Test
    @TestTransaction
    void persistUser_should_throw_DuplicateEntityException_when_username_exists() {
        User user1 = new User();
        user1.setUsername("duplicateUser");
        user1.setEmail("first@example.com");
        user1.setPassword("pass");
        userPersistenceAdapter.persistUser(user1);

        User user2 = new User();
        user2.setUsername("duplicateUser"); // Konflikt!
        user2.setEmail("second@example.com");
        user2.setPassword("pass");

        assertThrows(DuplicateEntityException.class, () -> {
            userPersistenceAdapter.persistUser(user2);
        });
    }


    @Test
    void readUserByUsername_should_find_existing_user() {
        Optional<User> user = userPersistenceAdapter.readUserByUsername("bob");

        assertTrue(user.isPresent());
        assertEquals("bob@example.com", user.get().getEmail());
    }

    @Test
    void readUserByUsername_should_return_empty_for_unknown_user() {
        Optional<User> user = userPersistenceAdapter.readUserByUsername("non-existent");
        assertTrue(user.isEmpty());
    }


    @Test
    @TestTransaction
    void updateUser_should_persist_changes() {
        User user = userPersistenceAdapter.readUserById(2L).get();
        user.setEmail("alice-new@example.com");

        userPersistenceAdapter.updateUser(user);

        User updated = userPersistenceAdapter.readUserById(2L).get();
        assertEquals("alice-new@example.com", updated.getEmail());
    }

    @Test
    @TestTransaction
    void updateUser_should_throw_NotFound_when_id_invalid() {
        User user = new User();
        user.setId(999L);
        user.setUsername("ghost");

        assertThrows(EntityNotFoundException.class, () -> {
            userPersistenceAdapter.updateUser(user);
        });
    }


    @Test
    @TestTransaction
    void deleteUserById_should_remove_user_from_db() {
        userPersistenceAdapter.deleteUserById(3L);

        Optional<User> deleted = userPersistenceAdapter.readUserById(3L);
        assertFalse(deleted.isPresent());
    }

    @Test
    void deleteUserById_should_throw_NotFound_if_missing() {
        assertThrows(EntityNotFoundException.class, () -> {
            userPersistenceAdapter.deleteUserById(8888L);
        });
    }
}