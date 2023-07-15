package com.quiz.user.config;

import com.quiz.user.entities.Role;
import com.quiz.user.entities.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.*;
import com.quiz.user.services.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;


import static org.mockito.Mockito.*;

public class JwtAuthenticationFilterTest {

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtService, userDetailsService);
    }

    @Test
    public void testDoFilterInternal_ValidJwt_SetsAuthentication() throws Exception {
        String jwt = "valid.jwt.token";
        String userEmail = "test@example.com";
        User userDetails = new User();
        userDetails.setRole(Role.USER);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtService.extractUsername(jwt)).thenReturn(userEmail);
        when(userDetailsService.loadUserByUsername(userEmail)).thenReturn(userDetails);
        when(jwtService.isTokenValid(jwt, userDetails)).thenReturn(true);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(userDetailsService).loadUserByUsername(userEmail);
        verify(jwtService).isTokenValid(jwt, userDetails);
        verify(filterChain).doFilter(request, response);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        assert SecurityContextHolder.getContext().getAuthentication() != null;
        assert SecurityContextHolder.getContext().getAuthentication().equals(authenticationToken);
    }

    @Test
    public void testDoFilterInternal_InvalidJwt_DoesNotSetAuthentication() throws Exception {
        String jwt = "invalid.jwt.token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtService.extractUsername(jwt)).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verifyZeroInteractions(userDetailsService, jwtService, filterChain);
        assert SecurityContextHolder.getContext().getAuthentication() == null;
    }

    @Test
    public void testDoFilterInternal_AuthHeaderNull_DoesNotSetAuthentication() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verifyZeroInteractions(userDetailsService, jwtService, filterChain);
        assert SecurityContextHolder.getContext().getAuthentication() == null;
    }

	private void verifyZeroInteractions(UserDetailsService userDetailsService2, JwtService jwtService2,
			FilterChain filterChain2) {
		// TODO Auto-generated method stub
		
	}
}
