package com.quiz.user.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.quiz.user.dao.UserDao;
import com.quiz.user.entities.Login;
import com.quiz.user.entities.LoginResponse;
import com.quiz.user.entities.User;
import com.quiz.user.entities.UserDTO;
import com.quiz.user.exceptions.ResourceAlreadyExistsException;
import com.quiz.user.exceptions.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authManager;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());
        when(userDao.findAll()).thenReturn(users);

        List<UserDTO> result = userService.getUsers();

        Assertions.assertEquals(users.size(), result.size());
        verify(userDao, times(1)).findAll();
    }

    @Test
    public void testRegisterUser_SuccessfulRegistration() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");

        UserDTO result = userService.registerUser(user);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(user.getEmail(), result.getEmail());
        Assertions.assertEquals("encodedPassword", user.getPassword());
        verify(userDao, times(1)).save(user);
    }

    @Test
    public void testRegisterUser_UserAlreadyExists() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Assertions.assertThrows(ResourceAlreadyExistsException.class, () -> {
            userService.registerUser(user);
        });
        
        verify(userDao, never()).save(any(User.class));
    }

    @Test
    public void testGetUsers_ValidUserId_ReturnsUserDTO() {
        int userId = 1;
        User user = new User();
        user.setUserId(userId);
        when(userDao.findById(userId)).thenReturn(Optional.of(user));

        UserDTO result = userService.getUsers(userId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(userId, result.getUserId());
        verify(userDao, times(1)).findById(userId);
    }

    @Test
    public void testGetUsers_InvalidUserId_ThrowsResourceNotFoundException() {
        int userId = 1;
        when(userDao.findById(userId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUsers(userId);
        });
        
        verify(userDao, times(1)).findById(userId);
    }

    @Test
    public void testLoginUser_ValidLogin_ReturnsLoginResponse() {
        String email = "test@example.com";
        String password = "password";

        Login request = new Login();
        request.setEmail(email);
        request.setPassword(password);

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email,
                password);
        when(authManager.authenticate(authenticationToken)).thenReturn(authenticationToken);
        when(userDao.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("testToken");

        LoginResponse result = userService.loginUser(request);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("success", result.getStatus());
        Assertions.assertEquals("Login successful", result.getMessage());
        Assertions.assertEquals(email, result.getUser().getEmail());
        Assertions.assertEquals("testToken", result.getToken());
    }

    @Test
    public void testLoginUser_UserNotFound_ThrowsResourceNotFoundException() {
        Login request = new Login();
        request.setEmail("nonexistent@example.com");
        request.setPassword("password");

        when(userDao.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            userService.loginUser(request);
        });
    }

    @Test
    public void testGetUserDetails_ExistingUser_ReturnsUser() {
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);

        when(userDao.findByEmail(email)).thenReturn(Optional.of(user));

        User result = userService.getUserDetails(email);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(email, result.getEmail());
    }

    @Test
    public void testGetUserDetails_NonExistingUser_ThrowsResourceNotFoundException() {
        String email = "nonexistent@example.com";

        when(userDao.findByEmail(email)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserDetails(email);
        });
    }
}
