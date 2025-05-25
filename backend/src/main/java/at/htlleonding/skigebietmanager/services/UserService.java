package at.htlleonding.skigebietmanager.services;

import at.htlleonding.skigebietmanager.entities.User;
import at.htlleonding.skigebietmanager.repositories.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserService {
    @Inject
    UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.listAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User createUser(User user) {
        userRepository.persist(user);
        return user;
    }

    @Transactional
    public User updateUser(Long id, User user) {
        User existingUser = userRepository.findById(id);
        if (existingUser != null) {
            existingUser.setName(user.getName());
            existingUser.setEmail(user.getEmail());
            userRepository.persist(existingUser);
            return existingUser;
        }
        return null;
    }

    @Transactional
    public boolean deleteUser(Long id) {
        return userRepository.deleteById(id);
    }
} 