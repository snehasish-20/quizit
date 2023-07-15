package com.quizapp.quiz.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.quizapp.quiz.entities.Submissions;
import com.quizapp.quiz.entities.User;
import com.quizapp.quiz.exceptions.UnauthorizedAccessException;
import com.quizapp.quiz.services.AuthenticationService;
import com.quizapp.quiz.services.SubmissionsService;

class SubmissionsControllerTest {

    @Mock
    private SubmissionsService submissionsService;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private SubmissionsController submissionsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetSubmissions() {
        // Mock data
        Long quizId = 1L;
        String authorization = "Bearer token";
        List<Submissions> submissions = List.of(new Submissions());
        when(authenticationService.validateRequest(authorization)).thenReturn(new User());
        when(submissionsService.getSubmissions(quizId, authorization)).thenReturn(submissions);
        // Call the controller method
        List<Submissions> result = submissionsController.getSubmissions(quizId, authorization);
        // Verify the result
        assertNotNull(result);
        assertEquals(submissions, result);
        // Verify that the validateRequest and getSubmissions methods were called
        verify(authenticationService).validateRequest(authorization);
        verify(submissionsService).getSubmissions(quizId, authorization);
    }

    @Test
    void testGetSubmissionsByUserId() {
        // Mock data
        String authorization = "Bearer token";
        User user = new User();
        List<HashMap<String, String>> submissionData = List.of(new HashMap<>());
        when(authenticationService.validateRequest(authorization)).thenReturn(user);
        when(authenticationService.validateRole(authorization, "USER")).thenReturn(true);
        when(submissionsService.getSubmissionsByUserId(user.getUserId())).thenReturn(submissionData);
        // Call the controller method
        List<HashMap<String, String>> result = submissionsController.getSubmissionsByUserId(authorization);
        // Verify the result
        assertNotNull(result);
        assertEquals(submissionData, result);
        // Verify that the validateRequest, validateRole, and getSubmissionsByUserId methods were called
        verify(authenticationService).validateRequest(authorization);
        verify(authenticationService).validateRole(authorization, "USER");
        verify(submissionsService).getSubmissionsByUserId(user.getUserId());
    }

//    @Test
//    void testGetSubmissionsByUserId_UnauthorizedAccessException() {
//        // Mock data
//        String authorization = "Bearer token";
//        User user = new User();
//        when(authenticationService.validateRequest(authorization)).thenReturn(user);
//        when(authenticationService.validateRole(authorization, "USER")).thenReturn(false);
//        // Call the controller method and verify that it throws UnauthorizedAccessException
//        assertThrows(UnauthorizedAccessException.class,
//                () -> submissionsController.getSubmissionsByUserId(authorization));
//        // Verify that the validateRequest and validateRole methods were called
//        verify(authenticationService).validateRequest(authorization);
//        verify(authenticationService).validateRole(authorization, "USER");
//        // Verify that the getSubmissionsByUserId method was not called
//        verify(submissionsService).getSubmissionsByUserId(user.getUserId());
//    }
}

