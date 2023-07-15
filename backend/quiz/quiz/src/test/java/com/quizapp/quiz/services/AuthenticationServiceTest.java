package com.quizapp.quiz.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.quizapp.quiz.entities.User;

@SpringBootTest
public class AuthenticationServiceTest {

	@Mock
	private RestTemplate restTemplate;

    @InjectMocks
    private AuthenticationService authenticationService;

    private final String validationUrl = "http://USER-SERVICE/users/token/validate";
    
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testValidateRequest() {
        // Mock data
        String token = "test-token";
        User expectedUser = new User();
        // Set properties of the expected user
        
        // Mock the restTemplate behavior
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<User> responseEntity = ResponseEntity.ok(expectedUser);
        when(restTemplate.exchange(eq(validationUrl), eq(HttpMethod.GET), eq(entity), eq(User.class)))
                .thenReturn(responseEntity);
        
        // Call the service method
        User result = authenticationService.validateRequest(token);
        
        // Assertions
        assertNotNull(result);
        // Add specific assertions for the properties of the user
    }
}
