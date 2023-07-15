package com.quizapp.quiz.controllers;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.quizapp.quiz.entities.Questions;
import com.quizapp.quiz.entities.Quiz;
import com.quizapp.quiz.entities.Submissions;
import com.quizapp.quiz.entities.User;
import com.quizapp.quiz.exceptions.UnauthorizedAccessException;
import com.quizapp.quiz.services.AuthenticationService;
import com.quizapp.quiz.services.QuizService;
import com.quizapp.quiz.services.SubmissionsService;

class QuizControllerTest {

    @Mock
    private QuizService quizService;

    @Mock
    private SubmissionsService submissionsService;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private QuizController quizController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllQuizzes_AdminRole() {
        // Mock data
        String authorization = "Bearer token";
        List<Quiz> quizzes = List.of(new Quiz());
        when(authenticationService.validateRole(authorization, "ADMIN")).thenReturn(true);
        when(quizService.getAllQuizzes()).thenReturn(quizzes);
        // Call the controller method
        List<Quiz> result = quizController.getAllQuizzes(authorization);
        // Verify the result
        assertNotNull(result);
        assertEquals(quizzes, result);
        // Verify that the validateRole and getAllQuizzes methods were called
        verify(authenticationService).validateRole(authorization, "ADMIN");
        verify(quizService).getAllQuizzes();
    }

    @Test
    void testGetAllQuizzes_UnauthorizedAccessException() {
        // Mock data
        String authorization = "Bearer token";
        when(authenticationService.validateRole(authorization, "ADMIN")).thenReturn(false);
        // Call the controller method and verify that it throws UnauthorizedAccessException
        assertThrows(UnauthorizedAccessException.class, () -> quizController.getAllQuizzes(authorization));
        // Verify that the validateRole method was called
        verify(authenticationService).validateRole(authorization, "ADMIN");
    }

    @Test
    void testGetQuiz() throws JsonProcessingException {
        // Mock data
        String authorization = "Bearer token";
        Long quizId = 1L;
        User user = new User();
        Quiz quiz = new Quiz();
        when(authenticationService.validateRequest(authorization)).thenReturn(user);
        when(quizService.getQuiz(quizId, user)).thenReturn(quiz);
        // Call the controller method
        Quiz result = quizController.getQuiz(authorization, quizId);
        // Verify the result
        assertNotNull(result);
        assertEquals(quiz, result);
        verify(authenticationService).validateRequest(authorization);
        verify(quizService).getQuiz(quizId, user);
    }

    @Test
    void testCreateQuiz_AdminRole() {
        // Mock data
        String authorization = "Bearer token";
        Quiz quiz = new Quiz();
        when(authenticationService.validateRole(authorization, "ADMIN")).thenReturn(true);
        when(quizService.createQuiz(quiz)).thenReturn(quiz);
        // Call the controller method
        Quiz result = quizController.createQuiz(quiz, authorization);
        // Verify the result
        assertNotNull(result);
        assertEquals(quiz, result);
        // Verify that the validateRole and createQuiz methods were called
        verify(authenticationService).validateRole(authorization, "ADMIN");
        verify(quizService).createQuiz(quiz);
    }

    @Test
    void testCreateQuiz_UnauthorizedAccessException() {
        // Mock data
        String authorization = "Bearer token";
        Quiz quiz = new Quiz();
        when(authenticationService.validateRole(authorization, "ADMIN")).thenReturn(false);
        // Call the controller method and verify that it throws UnauthorizedAccessException
        assertThrows(UnauthorizedAccessException.class, () -> quizController.createQuiz(quiz, authorization));
        // Verify that the validateRole method was called
        verify(authenticationService).validateRole(authorization, "ADMIN");
        // Verify that the createQuiz method was not called
    }

    @Test
    void testSubmitQuiz_ValidSubmission() {
      // Mock data
      String authorization = "Bearer token";
      Long quizId = 1L;
      User user = new User();
      user.setRole("USER");
      user.setUserId((int) 1L);
      List<Questions> submission = new ArrayList<>();
      Quiz quiz = new Quiz();
      quiz.setQuizId(quizId);
      Submissions expectedSubmission = new Submissions();
      expectedSubmission.setTotalCorrect((long) 80);
      expectedSubmission.setUserId((long) user.getUserId());
      expectedSubmission.setQuiz(quiz);
      
      // Mock authenticationService
      when(authenticationService.validateRequest(authorization)).thenReturn(user);
      
      // Mock quizService
      when(quizService.checkIfQuizExists(quizId)).thenReturn(true);
      when(quizService.submitQuiz(submission)).thenReturn(80L);
      
      // Mock submissionsService
      when(submissionsService.getSubmissionsByQuizAndUserId(quizId, user.getUserId())).thenReturn(null);
      when(submissionsService.submitQuizResults(expectedSubmission)).thenReturn(expectedSubmission);
      
      // Call the controller method
      Submissions result = quizController.submitQuiz(authorization, quizId, submission);
      
      // Verify the result
      assertNull(result);
    }
    
    @Test
    void testSubmitQuiz_UnauthorizedAccessException() {
      // Mock data
      String authorization = "Bearer token";
      Long quizId = 1L;
      User user = new User();
      user.setRole("ADMIN");
      user.setUserId((int) 1L);
      List<Questions> submission = new ArrayList<>();
      
      // Mock authenticationService
      when(authenticationService.validateRequest(authorization)).thenReturn(user);
      
      // Call the controller method and verify that it throws UnauthorizedAccessException
      assertThrows(UnauthorizedAccessException.class, () -> quizController.submitQuiz(authorization, quizId, submission));
      
      // Verify that the validateRequest method was called
      verify(authenticationService).validateRequest(authorization);
      
      // Verify that the checkIfQuizExists, submitQuiz, getSubmissionsByQuizAndUserId, and submitQuizResults methods were not called
      verifyNoInteractions(quizService);
      verifyNoInteractions(submissionsService);
    }

    @Test
    void testUpdateQuiz_AdminRole() {
        // Mock data
        String authorization = "Bearer token";
        Quiz quiz = new Quiz();
        when(authenticationService.validateRole(authorization, "ADMIN")).thenReturn(true);
        when(quizService.UpdateQuiz(quiz)).thenReturn(quiz);
        // Call the controller method
        Quiz result = quizController.updateQuiz(authorization, quiz);
        // Verify the result
        assertNotNull(result);
        assertEquals(quiz, result);
        // Verify that the validateRole and updateQuiz methods were called
        verify(authenticationService).validateRole(authorization, "ADMIN");
        verify(quizService).UpdateQuiz(quiz);
    }

    @Test
    void testUpdateQuiz_UnauthorizedAccessException() {
        // Mock data
        String authorization = "Bearer token";
        Quiz quiz = new Quiz();
        when(authenticationService.validateRole(authorization, "ADMIN")).thenReturn(false);
        // Call the controller method and verify that it throws UnauthorizedAccessException
        assertThrows(UnauthorizedAccessException.class, () -> quizController.updateQuiz(authorization, quiz));
        // Verify that the validateRole method was called
        verify(authenticationService).validateRole(authorization, "ADMIN");
    }

    @Test
    void testDeleteQuiz_AdminRole() {
        // Mock data
        String authorization = "Bearer token";
        Long quizId = 1L;
        when(authenticationService.validateRole(authorization, "ADMIN")).thenReturn(true);
        // Call the controller method
        ResponseEntity<String> result = quizController.deleteQuiz(authorization, quizId);
        // Verify the result
        assertNotNull(result);
        assertEquals("Quiz deleted sucessfully", result.getBody());
        // Verify that the validateRole and deleteQuiz methods were called
        verify(authenticationService).validateRole(authorization, "ADMIN");
        verify(quizService).deleteQuiz(quizId);
    }

    @Test
    void testDeleteQuiz_UnauthorizedAccessException() {
        // Mock data
        String authorization = "Bearer token";
        Long quizId = 1L;
        when(authenticationService.validateRole(authorization, "ADMIN")).thenReturn(false);
        // Call the controller method and verify that it throws UnauthorizedAccessException
        assertThrows(UnauthorizedAccessException.class, () -> quizController.deleteQuiz(authorization, quizId));
        // Verify that the validateRole method was called
        verify(authenticationService).validateRole(authorization, "ADMIN");
        // Verify that the deleteQuiz method was not called
    }
    
    @Test
    void testGetUserStats_ValidAuthorization() {
      // Mock data
      String authorization = "Bearer token";
      User user = new User();
      user.setRole("USER");
      user.setUserId((int) 1L);
      Map<String, Long> expectedStats = new HashMap<>();
      expectedStats.put("quizzesTaken", 5L);
      expectedStats.put("totalScore", 400L);
      
      // Mock authenticationService
      when(authenticationService.validateRequest(authorization)).thenReturn(user);
      
      // Mock quizService
      when(quizService.getUserStats(authorization, user.getUserId())).thenReturn(expectedStats);
      
      // Call the controller method
      Map<String, Long> result = quizController.getUserStats(authorization);
      
      // Verify the result
      assertNotNull(result);
      assertEquals(expectedStats, result);
      
      // Verify that the validateRequest and getUserStats methods were called
      verify(authenticationService).validateRequest(authorization);
      verify(quizService).getUserStats(authorization, user.getUserId());
    }
    
    @Test
    void testGetUserStats_UnauthorizedAccessException() {
      // Mock data
      String authorization = "Bearer token";
      User user = new User();
      user.setRole("ADMIN");
      user.setUserId((int) 1L);
      
      // Mock authenticationService
      when(authenticationService.validateRequest(authorization)).thenReturn(user);
      
      // Call the controller method and verify that it throws UnauthorizedAccessException
      assertThrows(UnauthorizedAccessException.class, () -> quizController.getUserStats(authorization));
      
      // Verify that the validateRequest method was called
      verify(authenticationService).validateRequest(authorization);
      
      // Verify that the getUserStats method was not called
      verifyNoInteractions(quizService);
    }
    
    @Test
    void testGetAdminStats_ValidAuthorization() {
      // Mock data
      String authorization = "Bearer token";
      Map<String, Long> expectedStats = new HashMap<>();
      expectedStats.put("totalQuizzes", 10L);
      expectedStats.put("totalSubmissions", 50L);
      
      // Mock authenticationService
      when(authenticationService.validateRole(authorization, "ADMIN")).thenReturn(true);
      
      // Mock quizService
      when(quizService.getAdminStats(authorization)).thenReturn(expectedStats);
      
      // Call the controller method
      Map<String, Long> result = quizController.getAdminStats(authorization);
      
      // Verify the result
      assertNotNull(result);
      assertEquals(expectedStats, result);
      
      // Verify that the validateRole and getAdminStats methods were called
      verify(authenticationService).validateRole(authorization, "ADMIN");
      verify(quizService).getAdminStats(authorization);
    }
    
    @Test
    void testGetAdminStats_UnauthorizedAccessException() {
      // Mock data
      String authorization = "Bearer token";
      
      // Mock authenticationService
      when(authenticationService.validateRole(authorization, "ADMIN")).thenReturn(false);
      
      // Call the controller method and verify that it throws UnauthorizedAccessException
      assertThrows(UnauthorizedAccessException.class, () -> quizController.getAdminStats(authorization));
      
      // Verify that the validateRole method was called
      verify(authenticationService).validateRole(authorization, "ADMIN");
      
      // Verify that the getAdminStats method was not called
      verifyNoInteractions(quizService);
    }

//    @Test
//    void testGetSubmissionsByQuizId_AdminRole() {
//        // Mock data
//        String authorization = "Bearer token";
//        Long quizId = 1L;
//        List<Submissions> submissions = List.of(new Submissions());
//        when(authenticationService.validateRole(authorization, "ADMIN")).thenReturn(true);
//        when(submissionsService.getSubmissionsByQuizId(quizId)).thenReturn(submissions);
//        // Call the controller method
//        List<Submissions> result = quizController.getSubmissionsByQuizId(authorization, quizId);
//        // Verify the result
//        assertNotNull(result);
//        assertEquals(submissions, result);
//        // Verify that the validateRole and getSubmissionsByQuizId methods were called
//        verify(authenticationService).validateRole(authorization, "ADMIN");
//        verify(submissionsService).getSubmissionsByQuizId(quizId);
//    }
//
//    @Test
//    void testGetSubmissionsByQuizId_UnauthorizedAccessException() {
//        // Mock data
//        String authorization = "Bearer token";
//        Long quizId = 1L;
//        when(authenticationService.validateRole(authorization, "ADMIN")).thenReturn(false);
//        // Call the controller method and verify that it throws UnauthorizedAccessException
//        assertThrows(UnauthorizedAccessException.class, () -> quizController.getSubmissionsByQuizId(authorization, quizId));
//        // Verify that the validateRole method was called
//        verify(authenticationService).validateRole(authorization, "ADMIN");
//        // Verify that the getSubmissionsByQuizId method was not called
//        verify(submissionsService).getSubmissionsByQuizId(quizId);
//    }
//
//    @Test
//    void testGetSubmissionsByUserId_AdminRole() {
//        // Mock data
//        String authorization = "Bearer token";
//        Long userId = 1L;
//        List<Submissions> submissions = List.of(new Submissions());
//        when(authenticationService.validateRole(authorization, "ADMIN")).thenReturn(true);
//        when(submissionsService.getSubmissionsByUserId(userId)).thenReturn(submissions);
//        // Call the controller method
//        List<Submissions> result = quizController.getSubmissionsByUserId(authorization, userId);
//        // Verify the result
//        assertNotNull(result);
//        assertEquals(submissions, result);
//        // Verify that the validateRole and getSubmissionsByUserId methods were called
//        verify(authenticationService).validateRole(authorization, "ADMIN");
//        verify(submissionsService).getSubmissionsByUserId(userId);
//    }
//
//    @Test
//    void testGetSubmissionsByUserId_UnauthorizedAccessException() {
//        // Mock data
//        String authorization = "Bearer token";
//        Long userId = 1L;
//        when(authenticationService.validateRole(authorization, "ADMIN")).thenReturn(false);
//        // Call the controller method and verify that it throws UnauthorizedAccessException
//        assertThrows(UnauthorizedAccessException.class, () -> quizController.getSubmissionsByUserId(authorization, userId));
//        // Verify that the validateRole method was called
//        verify(authenticationService).validateRole(authorization, "ADMIN");
//        // Verify that the getSubmissionsByUserId method was not called
//        verify(submissionsService).getSubmissionsByUserId(userId);
//    }

//    @Test
//    void testGetQuizResults_AdminRole() {
//        // Mock data
//        String authorization = "Bearer token";
//        Long submissionId = 1L;
//        Map<String, Integer> quizResults = Map.of("Question 1", 1, "Question 2", 0);
//        when(authenticationService.validateRole(authorization, "ADMIN")).thenReturn(true);
//        when(submissionsService.getQuizResults(submissionId)).thenReturn(quizResults);
//        // Call the controller method
//        Map<String, Integer> result = quizController.getQuizResults(authorization, submissionId);
//        // Verify the result
//        assertNotNull(result);
//        assertEquals(quizResults, result);
//        // Verify that the validateRole and getQuizResults methods were called
//        verify(authenticationService).validateRole(authorization, "ADMIN");
//        verify(submissionsService).getQuizResults(submissionId);
//    }

//    @Test
//    void testGetQuizResults_UnauthorizedAccessException() {
//        // Mock data
//        String authorization = "Bearer token";
//        Long submissionId = 1L;
//        when(authenticationService.validateRole(authorization, "ADMIN")).thenReturn(false);
//        // Call the controller method and verify that it throws UnauthorizedAccessException
//        assertThrows(UnauthorizedAccessException.class, () -> quizController.getQuizResults(authorization, submissionId));
//        // Verify that the validateRole method was called
//        verify(authenticationService).validateRole(authorization, "ADMIN");
//        // Verify that the getQuizResults method was not called
//        verify(submissionsService).getQuizResults(submissionId);
//    }
}
