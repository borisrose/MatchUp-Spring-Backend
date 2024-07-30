package com.matchup.app.controller;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.matchup.app.model.User;
import com.matchup.app.service.CustomEncoderService.CustomEncoderService;
import com.matchup.app.service.CustomTokenService.CustomTokenService;
import com.matchup.app.service.UserService.UserService;
import com.matchup.app.controller.UserController.LoginBody;
import com.matchup.app.controller.UserController.RegisterBody;
import com.matchup.app.controller.UserController.ResponseToken;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CustomTokenService customTokenService;

    @Mock
    private UserService userService;

    @Mock
    private CustomEncoderService customEncoderService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        userController = new UserController(authenticationManager, customTokenService, userService, customEncoderService);
    }

    @Test
    void testLoginUser_Success() {
        // Arrange
        LoginBody loginBody = new LoginBody("test@example.com", "password");
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(customTokenService.generateToken(any(Authentication.class))).thenReturn("testToken");

        // Act
        ResponseEntity<ResponseToken> response = userController.loginUser(loginBody);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("testToken", response.getBody().token());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(customTokenService, times(1)).generateToken(any(Authentication.class));
    }

    @Test
    void testLoginUser_Failure() {
        // Arrange
        LoginBody loginBody = new LoginBody("test@example.com", "password");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new RuntimeException());

        // Act
        ResponseEntity<ResponseToken> response = userController.loginUser(loginBody);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(customTokenService, times(0)).generateToken(any(Authentication.class));
    }

    @Test
    void testRegisterUser_Success() {
        // Arrange
        RegisterBody registerBody = new RegisterBody("John", "Doe", "john.doe@example.com", "password");
        User newUser = new User("John", "Doe", "john.doe@example.com", "password");
        when(userService.createUser(anyString(), anyString(), anyString(), anyString())).thenReturn(newUser);
        when(customTokenService.generateRegisteringToken(anyString())).thenReturn("registerToken");

        // Act
        ResponseEntity<ResponseToken> response = userController.registerUser(registerBody);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("registerToken", response.getBody().token());
        verify(userService, times(1)).createUser(anyString(), anyString(), anyString(), anyString());
        verify(customTokenService, times(1)).generateRegisteringToken(anyString());
    }

    @Test
    void testRegisterUser_Failure() {
        // Arrange
        RegisterBody registerBody = new RegisterBody("John", "Doe", "john.doe@example.com", "password");
        when(userService.createUser(anyString(), anyString(), anyString(), anyString())).thenReturn(null);

        // Act
        ResponseEntity<ResponseToken> response = userController.registerUser(registerBody);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(userService, times(1)).createUser(anyString(), anyString(), anyString(), anyString());
        verify(customTokenService, times(0)).generateRegisteringToken(anyString());
    }
}
