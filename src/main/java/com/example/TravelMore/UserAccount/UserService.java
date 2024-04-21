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
        Optional<User> userByEmail = userRepository.findUserByUserEmail(user.getUserEmail());
        if (userByEmail.isPresent()) {
            throw new IllegalStateException("Email address is already registered.");
        }
        String password = user.getUserPassword();
        if (password == null || password.length() < 8 || !isPasswordComplex(password)) {
            throw new IllegalArgumentException("Password must be at least 8 characters long and contain " +
                    "at least one uppercase letter, one lowercase letter, one digit, and one special character.");
        }

        String hashedPassword = passwordEncoder.encode(password);
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
        Optional<User> optionalUser = userRepository.findUserByUserEmail(loginRequest.getUserEmail());
        return optionalUser.filter(user -> passwordEncoder.matches(loginRequest.getUserPassword(), user.getUserPassword()))
                .orElse(null);
    }

    private boolean isPasswordComplex(String password) {
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        return password.matches(passwordRegex);
    }

    public void updateUserAvatar(Long userId, String avatarFileName) {
        User user = userRepository.findById(userId).orElse(null);
        System.out.println(avatarFileName + "sff");

        if (user != null) {
            user.setUrl(avatarFileName);
            System.out.println(user.getUrl());
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }
}
