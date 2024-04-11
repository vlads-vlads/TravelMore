package com.example.TravelMore.UserAccount;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createUser(String username, String password) {
        String hashedPassword = passwordEncoder.encode(password);
        User user = new User();
        user.setUserName(username);
        user.setUserPassword(hashedPassword);
        userRepository.save(user);
    }
}
