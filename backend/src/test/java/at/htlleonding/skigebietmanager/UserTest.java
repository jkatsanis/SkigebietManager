package at.htlleonding.skigebietmanager;

import at.htlleonding.skigebietmanager.entities.User;
import at.htlleonding.skigebietmanager.repositories.UserRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class UserTest {

    @Inject
    UserRepository userRepository;

    @Test
    @Transactional
    public void testCreateAndFindUser() {
        // Create a new user with a unique email
        String uniqueEmail = "test-" + UUID.randomUUID() + "@example.com";
        User user = new User("Test User", uniqueEmail);
        userRepository.persist(user);

        // Find the user by ID
        User foundUser = userRepository.findById(user.getId());
        assertNotNull(foundUser);
        assertEquals("Test User", foundUser.getName());
        assertEquals(uniqueEmail, foundUser.getEmail());

        // Find the user by email
        Optional<User> userByEmail = userRepository.findByEmail(uniqueEmail);
        assertTrue(userByEmail.isPresent());
        assertEquals(user.getId(), userByEmail.get().getId());
    }

    @Test
    @Transactional
    public void testUpdateUser() {
        // Create a new user with a unique email
        String uniqueEmail = "original-" + UUID.randomUUID() + "@example.com";
        User user = new User("Original Name", uniqueEmail);
        userRepository.persist(user);

        // Update the user
        String updatedEmail = "updated-" + UUID.randomUUID() + "@example.com";
        user.setName("Updated Name");
        user.setEmail(updatedEmail);
        userRepository.persist(user);

        // Find and verify the updated user
        User updatedUser = userRepository.findById(user.getId());
        assertNotNull(updatedUser);
        assertEquals("Updated Name", updatedUser.getName());
        assertEquals(updatedEmail, updatedUser.getEmail());
    }

    @Test
    @Transactional
    public void testDeleteUser() {
        // Create a new user with a unique email
        String uniqueEmail = "delete-" + UUID.randomUUID() + "@example.com";
        User user = new User("Delete Test", uniqueEmail);
        userRepository.persist(user);
        Long userId = user.getId();

        // Delete the user
        boolean deleted = userRepository.deleteById(userId);
        assertTrue(deleted);

        // Verify the user is deleted
        User deletedUser = userRepository.findById(userId);
        assertNull(deletedUser);
    }

    @Test
    @Transactional
    public void testListAllUsers() {
        // Create multiple users with unique emails
        String email1 = "user1-" + UUID.randomUUID() + "@example.com";
        String email2 = "user2-" + UUID.randomUUID() + "@example.com";
        User user1 = new User("User 1", email1);
        User user2 = new User("User 2", email2);
        userRepository.persist(user1);
        userRepository.persist(user2);

        // Get all users
        List<User> users = userRepository.listAll();
        assertTrue(users.size() >= 2);
        assertTrue(users.stream().anyMatch(u -> u.getEmail().equals(email1)));
        assertTrue(users.stream().anyMatch(u -> u.getEmail().equals(email2)));
    }
} 