package com.example.TravelMore.UserAccount;

import com.example.TravelMore.model.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public User createUser(User user) {
        Optional<User> userById = userRepository.findUserByUserEmail(user.getUserEmail());
        if (userById.isPresent()) {
            throw new IllegalStateException("email taken");
        }

        String hashedPassword = passwordEncoder.encode(user.getUserPassword());
        user.setUserPassword(hashedPassword);
        userRepository.save(user);
        return user;
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElse(null);
    }

    public void deleteUser(Long userId) {
        boolean exists = userRepository.existsById(userId);
        if (!exists) {
            throw new IllegalStateException(
                    "account with id " + userId + " does not exists");
        }
        userRepository.deleteById(userId);
    }

    public User updateUser(Long userId, User userUpdates) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (userUpdates.getUserName() != null) {
            existingUser.setUserName(userUpdates.getUserName());
        }
        if (userUpdates.getUserEmail() != null) {
            existingUser.setUserEmail(userUpdates.getUserEmail());
        }
        if (userUpdates.getUserPassword() != null) {
            String hashedPassword = passwordEncoder.encode(userUpdates.getUserPassword());
            existingUser.setUserPassword(hashedPassword);
        }

        return userRepository.save(existingUser);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User authenticateUser(LoginRequest loginRequest) {
        Optional<User> optionalUser = userRepository.findUserByUserEmail(loginRequest.getEmail());
        return optionalUser.filter(user -> passwordEncoder.matches(loginRequest.getPassword(), user.getUserPassword()))
                .orElse(null);
    }
}
