package com.example.TravelMore.UserAccount;

import com.example.TravelMore.model.LoginRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void testCreateUser_ValidPassword() {
        // Arrange
        User user = new User();
        user.setUserEmail("test@example.com");
        user.setUserName("testUser");
        user.setUserPassword("ValidPassword1@");
        when(userRepository.findUserByUserEmail(user.getUserEmail())).thenReturn(Optional.empty());

        // Act
        User result = userService.createUser(user);

        // Assert
        assertNotNull(result);
        assertEquals(user.getUserName(), result.getUserName());
        assertEquals(user.getUserEmail(), result.getUserEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testCreateUser_EmailAlreadyRegistered() {
        // Arrange
        User user = new User();
        when(userRepository.findUserByUserEmail(user.getUserEmail())).thenReturn(Optional.of(user));

        // Act and Assert
        assertThrows(IllegalStateException.class, () -> userService.createUser(user));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testCreateUser_InvalidPassword() {
        // Arrange
        User user = new User();
        user.setUserEmail("test@example.com");
        user.setUserName("testUser");
        user.setUserPassword("invalid");
        when(userRepository.findUserByUserEmail(anyString())).thenReturn(Optional.empty());

        // Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));
        assertEquals("Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special character.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class)); // Ensure that userRepository.save() is never called
    }

    @Test
    void testGetUserById() {
        // Arrange
        Long userId = 1L;
        User expectedUser = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        // Act
        User result = userService.getUserById(userId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedUser, result);
    }

    @Test
    void testGetUserById_NotFound() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        User result = userService.getUserById(userId);

        // Assert
        assertNull(result);
    }

    @Test
    void testDeleteUser() {
        // Arrange
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);

        // Act
        userService.deleteUser(userId);

        // Assert
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void testDeleteUser_NotFound() {
        // Arrange
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(false);

        // Act and Assert
        assertThrows(IllegalStateException.class, () -> userService.deleteUser(userId));
        verify(userRepository, never()).deleteById(userId);
    }

    @Test
    void testUpdateUser() throws NoSuchFieldException, IllegalAccessException {
        // Arrange
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setUserName("testUser");
        existingUser.setUserEmail("test@example.com");
        existingUser.setUserPassword("ValidPassword1!");

        User userUpdates = new User();
        userUpdates.setUserName("updatedUser");
        userUpdates.setUserEmail("updated@example.com");
        userUpdates.setUserPassword("UpdatedPassword1!");
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        // Act
        User updatedUser = userService.updateUser(userId, userUpdates);

        // Assert
        assertNotNull(updatedUser);
        assertEquals(userUpdates.getUserName(), updatedUser.getUserName());
        assertEquals(userUpdates.getUserEmail(), updatedUser.getUserEmail());
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void testUpdateUser_NotFound() {
        // Arrange
        Long userId = 1L;
        User userUpdates = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(userId, userUpdates));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testGetUsers() {
        // Arrange
        List<User> expectedUsers = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(expectedUsers);

        // Act
        List<User> result = userService.getUsers();

        // Assert
        assertEquals(expectedUsers, result);
    }

    @Test
    void testAuthenticateUser() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        User user = new User();
        user.setUserPassword("hashedPassword");
        when(userRepository.findUserByUserEmail(loginRequest.getUserEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getUserPassword(), user.getUserPassword())).thenReturn(true);

        // Act
        User result = userService.authenticateUser(loginRequest);

        // Assert
        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void testAuthenticateUser_InvalidPassword() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        User user = new User();
        user.setUserPassword("hashedPassword");
        when(userRepository.findUserByUserEmail(loginRequest.getUserEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getUserPassword(), user.getUserPassword())).thenReturn(false);

        // Act
        User result = userService.authenticateUser(loginRequest);

        // Assert
        assertNull(result);
    }

}