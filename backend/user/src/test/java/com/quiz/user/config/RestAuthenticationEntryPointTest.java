package com.quiz.user.config;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RestAuthenticationEntryPointTest {

    @Test
    public void testCommence() throws Exception {
        RestAuthenticationEntryPoint entryPoint = new RestAuthenticationEntryPoint();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException authenticationException = null; // Mock or create an instance of AuthenticationException if needed

        entryPoint.commence(request, response, authenticationException);

        assertEquals(401, response.getStatus());
        assertEquals("application/json", response.getContentType());
    }
}