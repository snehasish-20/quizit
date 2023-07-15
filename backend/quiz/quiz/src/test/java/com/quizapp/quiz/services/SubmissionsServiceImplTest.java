package com.quizapp.quiz.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.quizapp.quiz.dao.QuizRepository;
import com.quizapp.quiz.dao.SubmissionsRepository;
import com.quizapp.quiz.entities.Submissions;
import com.quizapp.quiz.entities.User;

class SubmissionsServiceImplTest {

    @Mock
    private SubmissionsRepository submissionsRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private QuizService quizService;

    @Mock
    private QuizRepository quizRepository;

    @InjectMocks
    private SubmissionsServiceImpl submissionsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSubmitQuizResults() {
        // Mock data
        Submissions submission = new Submissions();
        when(submissionsRepository.save(submission)).thenReturn(submission);
        // Call the service method
        Submissions result = submissionsService.submitQuizResults(submission);
        // Verify the result
        assertNotNull(result);
        assertEquals(submission, result);
        // Verify that the save method was called
        verify(submissionsRepository).save(submission);
    }

    @Test
    void testGetSubmissions() {
        // Mock data
        Long quizId = 1L;
        String authorization = "Bearer token";
        List<Submissions> submissions = new ArrayList<>();
        when(quizService.checkIfQuizExists(quizId)).thenReturn(true);
        when(submissionsRepository.getAllByQuizId(quizId)).thenReturn(submissions);
        ResponseEntity<User> userResponse = ResponseEntity.ok(new User());
        when(restTemplate.exchange(
                "http://USER-SERVICE/users?userId={userId}",
                HttpMethod.GET,
                new HttpEntity<>(createHeaders(authorization)),
                User.class,
                1L
        )).thenReturn(userResponse);
        // Call the service method
        List<Submissions> result = submissionsService.getSubmissions(quizId, authorization);
        // Verify the result
        assertNotNull(result);
        assertEquals(submissions, result);
        // Verify that the getAllByQuizId method was called
        verify(submissionsRepository).getAllByQuizId(quizId);
    }

    @Test
    void testGetSubmissionsByQuizAndUserId() {
        // Mock data
        long quizId = 1L;
        long userId = 1L;
        Submissions submission = new Submissions();
        when(submissionsRepository.getSubmissionsByQuizAndUserId(quizId, userId)).thenReturn(submission);
        // Call the service method
        Submissions result = submissionsService.getSubmissionsByQuizAndUserId(quizId, userId);
        // Verify the result
        assertNotNull(result);
        assertEquals(submission, result);
        // Verify that the getSubmissionsByQuizAndUserId method was called
        verify(submissionsRepository).getSubmissionsByQuizAndUserId(quizId, userId);
    }

    @Test
    void testGetSubmissionsCount() {
        // Mock data
        long count = 5L;
        when(submissionsRepository.count()).thenReturn(count);
        // Call the service method
        Long result = submissionsService.getSubmissionsCount();
        // Verify the result
        assertNotNull(result);
        assertEquals(count, result);
        // Verify that the count method was called
        verify(submissionsRepository).count();
    }

    @Test
    void testGetUserSubmissionsCount() {
        // Mock data
        Integer userId = 1;
        List<Submissions> submissions = new ArrayList<>();
        when(submissionsRepository.getSubmissionsByUserId(userId)).thenReturn(submissions);
        // Call the service method
        Long result = submissionsService.getUserSubmissionsCount(userId);
        // Verify the result
        assertNotNull(result);
        assertEquals(submissions.size(), result);
        // Verify that the getSubmissionsByUserId method was called
        verify(submissionsRepository).getSubmissionsByUserId(userId);
    }

//    @Test
//    void testGetSubmissionsByUserId() {
//        // Mock data
//        Integer userId = 1;
//        Submissions submission = new Submissions();
//        Quiz quiz = new Quiz();
//        quiz.setQuizId(1L);
//        quiz.setQuizName("Quiz 1");
//        when(submissionsRepository.getSubmissionsByUserId(userId)).thenReturn(List.of(submission));
//        when(quizRepository.findById(quiz.getQuizId())).thenReturn(java.util.Optional.of(quiz));
//        // Call the service method
//        List<HashMap<String, String>> result = submissionsService.getSubmissionsByUserId(userId);
//        // Verify the result
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        HashMap<String, String> submissionData = result.get(0);
//        assertEquals(String.valueOf(submission.getTotalCorrect()), submissionData.get("score"));
//        assertEquals(quiz.getQuizName(), submissionData.get("quizName"));
//        assertEquals(String.valueOf(quiz.getQuizId()), submissionData.get("quizId"));
//        // Verify that the getSubmissionsByUserId and findById methods were called
//        verify(submissionsRepository).getSubmissionsByUserId(userId);
//        verify(quizRepository).findById(quiz.getQuizId());
//    }

    private HttpHeaders createHeaders(String authorization) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorization);
        return headers;
    }
}

