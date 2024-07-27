package com.matchup.app.service.UserServiceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.matchup.app.model.User;
import com.matchup.app.repository.UserRepository;
import com.matchup.app.service.CustomEncoderService.CustomEncoderService;
import com.matchup.app.service.UserService.UserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CustomEncoderService customEncoderService;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        // This will be called before each test method.
        userService = new UserServiceImpl(userRepository, customEncoderService);
    }

    @Test
    void testCreateUser() {
        // Arrange
        String firstname = "John";
        String lastname = "Doe";
        String email = "john.doe@example.com";
        String password = "password";

        User newUser = new User(firstname, lastname, email, password);
    }

    @Test
    void testGetUserByEmail() {
        // Arrange
        String email = "john.doe@example.com";
        User user = new User("John", "Doe", email, "password");
        when(userRepository.findByEmail(email)).thenReturn(user);

        // Act
        User foundUser = userService.getUserByEmail(email);

        // Assert
        assertEquals(user, foundUser);
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testGetAllUsers() {
        // Arrange
        List<User> users = List.of(new User("John", "Doe", "john.doe@example.com", "password"));
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<User> foundUsers = userService.getAllUsers();

        // Assert
        assertEquals(users, foundUsers);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetOneUserById() {
        // Arrange
        String id = "1";
        Integer identifier = Integer.parseInt(id);
        Optional<User> user = Optional.of(new User("John", "Doe", "john.doe@example.com", "password"));
        when(userRepository.findById(identifier)).thenReturn(user);

        // Act
        Optional<User> foundUser = userService.getOneUserById(id);

        // Assert
        assertEquals(user, foundUser);
        verify(userRepository, times(1)).findById(identifier);
    }

    @Test
    void testDeleteUserById() {
        // Arrange
        String id = "1";
        Integer identifier = Integer.parseInt(id);
        Optional<User> user = Optional.of(new User("John", "Doe", "john.doe@example.com", "password"));
        when(userRepository.findById(identifier)).thenReturn(user);

        // Act
        Optional<User> deletedUser = userService.deleteUserById(id);

        // Assert
        assertEquals(user, deletedUser);
        verify(userRepository, times(1)).findById(identifier);
    }
}
