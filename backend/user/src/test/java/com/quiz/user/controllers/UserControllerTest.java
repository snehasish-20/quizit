package com.quiz.user.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.quiz.user.entities.Login;
import com.quiz.user.entities.LoginResponse;
import com.quiz.user.entities.Role;
import com.quiz.user.entities.User;
import com.quiz.user.entities.UserDTO;
import com.quiz.user.exceptions.AuthHeaderNotFoundException;
import com.quiz.user.services.JwtService;
import com.quiz.user.services.UserService;

@SpringBootTest
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private userController userController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUsers_ReturnsListOfUsers() {
        List<UserDTO> users = new ArrayList<>();
        users.add(new UserDTO());
        users.add(new UserDTO());
        when(userService.getUsers()).thenReturn(users);

        List<UserDTO> result = userController.getUsers();

        Assertions.assertEquals(users.size(), result.size());
        verify(userService, times(1)).getUsers();
    }

    @Test
    public void testGetUserById_ValidUserId_ReturnsUserDTO() {
        int userId = 1;
        UserDTO userDTO = new UserDTO();
        when(userService.getUsers(userId)).thenReturn(userDTO);

        UserDTO result = userController.getUserById(userId);

        Assertions.assertNotNull(result);
        verify(userService, times(1)).getUsers(userId);
    }

    @Test
    public void testRegisterUser_ValidUser_ReturnsUserDTO() {
        User user = new User();
        user.setRole(Role.USER);
        UserDTO userDTO = new UserDTO();
        when(userService.registerUser(user)).thenReturn(userDTO);

        UserDTO result = userController.registerUser(user);

        Assertions.assertNotNull(result);
        verify(userService, times(1)).registerUser(user);
    }

    @Test
    public void testLoginUser_ValidLogin_ReturnsResponseEntityWithToken() {
        Login request = new Login();
        LoginResponse response = new LoginResponse();
        response.setToken("testToken");
        when(userService.loginUser(request)).thenReturn(response);

        ResponseEntity<LoginResponse> result = userController.loginUser(request);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals("Bearer testToken", result.getHeaders().get("Authorization").get(0));
        Assertions.assertEquals(response, result.getBody());
        verify(userService, times(1)).loginUser(request);
    }

    @Test
    public void testValidateToken_ValidAuthorizationHeader_ReturnsUser() {
        String authorization = "Bearer testToken";
        User user = new User();
        when(userService.getUserDetails(any())).thenReturn(user);

        User result = userController.validateToken(authorization);

        Assertions.assertNotNull(result);
        verify(userService, times(1)).getUserDetails(any());
    }

    @Test
    public void testValidateToken_InvalidAuthorizationHeader_ThrowsAuthHeaderNotFoundException() {
        String authorization = "InvalidToken";

        Assertions.assertThrows(AuthHeaderNotFoundException.class, () -> {
            userController.validateToken(authorization);
        });
        verify(userService, never()).getUserDetails(any());
    }
}
