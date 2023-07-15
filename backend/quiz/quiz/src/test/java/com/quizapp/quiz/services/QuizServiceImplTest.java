package com.quizapp.quiz.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.quizapp.quiz.dao.QuizRepository;
import com.quizapp.quiz.entities.Questions;
import com.quizapp.quiz.entities.Quiz;
import com.quizapp.quiz.entities.User;
import com.quizapp.quiz.exceptions.ResourceNotFoundException;

@SpringBootTest
public class QuizServiceImplTest {

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private QuestionsService questionsService;

    @Mock
    private SubmissionsService submissionsService;

    @Mock
    private RestTemplate restTemplate;

    private QuizServiceImpl quizService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        quizService = new QuizServiceImpl();
        quizService.setQuizRepository(quizRepository);
        quizService.setQuestionsService(questionsService);
        quizService.setSubmissionsService(submissionsService);
        quizService.setRestTemplate(restTemplate);
    }

    @Test
    public void testGetQuiz_WhenQuizExistsAndUserIsAdmin() {
        // Mock data
        Long quizId = 1L;
        User user = new User();
        user.setRole("ADMIN");
        Quiz quiz = new Quiz();
        quiz.setQuizId(quizId);
        // Set up the behavior of the mock repository
        when(quizRepository.findByQuizId(quizId)).thenReturn(Optional.of(quiz));
        // Call the service method
        Quiz actualQuiz = quizService.getQuiz(quizId, user);
        // Verify the result
        assertEquals(quiz, actualQuiz);
    }

    @Test
    public void testGetQuiz_WhenQuizExistsAndUserIsUserAndQuizIsActive() {
        // Mock data
        Long quizId = 1L;
        User user = new User();
        user.setRole("USER");
        Quiz quiz = new Quiz();
        quiz.setQuizId(quizId);
        quiz.setQuizActive(true);
        List<Questions> questions = new ArrayList<>();
        questions.add(new Questions());
        // Set up the behavior of the mock repository
        when(quizRepository.findByQuizId(quizId)).thenReturn(Optional.of(quiz));
        when(questionsService.filterQuestionsForUser(quiz.getQuestions())).thenReturn(questions);
        // Call the service method
        Quiz actualQuiz = quizService.getQuiz(quizId, user);
        // Verify the result
        assertEquals(questions, actualQuiz.getQuestions());
    }

    @Test
    public void testGetQuiz_WhenQuizExistsAndUserIsUserAndQuizIsInactive() {
        // Mock data
        Long quizId = 1L;
        User user = new User();
        user.setRole("USER");
        Quiz quiz = new Quiz();
        quiz.setQuizId(quizId);
        quiz.setQuizActive(false);
        // Set up the behavior of the mock repository
        when(quizRepository.findByQuizId(quizId)).thenReturn(Optional.of(quiz));
        // Call the service method and verify the exception
        assertThrows(ResourceNotFoundException.class, () -> quizService.getQuiz(quizId, user));
    }

    @Test
    public void testGetQuiz_WhenQuizDoesNotExist() {
        // Mock data
        Long quizId = 1L;
        User user = new User();
        user.setRole("ADMIN");
        // Set up the behavior of the mock repository
        when(quizRepository.findByQuizId(quizId)).thenReturn(Optional.empty());
        // Call the service method and verify the exception
        assertThrows(ResourceNotFoundException.class, () -> quizService.getQuiz(quizId, user));
    }

    @Test
    public void testCreateQuiz() {
        // Mock data
        Quiz quiz = new Quiz();
        // Set up the behavior of the mock repository
        when(quizRepository.save(quiz)).thenReturn(quiz);
        // Call the service method
        Quiz createdQuiz = quizService.createQuiz(quiz);
        // Verify the result
        assertEquals(quiz, createdQuiz);
    }

    @Test
    public void testUpdateQuiz_WhenQuizExists() {
        // Mock data
        Long quizId = 1L;
        Quiz quiz = new Quiz();
        quiz.setQuizId(quizId);
        // Set up the behavior of the mock repository
        when(quizRepository.findByQuizId(quizId)).thenReturn(Optional.of(quiz));
        when(quizRepository.save(quiz)).thenReturn(quiz);
        // Call the service method
        Quiz updatedQuiz = quizService.UpdateQuiz(quiz);
        // Verify the result
        assertEquals(quiz, updatedQuiz);
    }

    @Test
    public void testUpdateQuiz_WhenQuizDoesNotExist() {
        // Mock data
        Long quizId = 1L;
        Quiz quiz = new Quiz();
        quiz.setQuizId(quizId);
        // Set up the behavior of the mock repository
        when(quizRepository.findByQuizId(quizId)).thenReturn(Optional.empty());
        // Call the service method and verify the exception
        assertThrows(ResourceNotFoundException.class, () -> quizService.UpdateQuiz(quiz));
    }

    @Test
    public void testGetAllQuizzes() {
        // Mock data
        List<Quiz> quizzes = new ArrayList<>();
        quizzes.add(new Quiz());
        quizzes.add(new Quiz());
        // Set up the behavior of the mock repository
        when(quizRepository.findAll()).thenReturn(quizzes);
        // Call the service method
        List<Quiz> actualQuizzes = quizService.getAllQuizzes();
        // Verify the result
        assertEquals(quizzes, actualQuizzes);
    }

    @Test
    public void testSubmitQuiz_WhenAllAnswersAreCorrect() {
        // Mock data
        List<Questions> submission = new ArrayList<>();
        Questions question1 = new Questions();
        question1.setSubmittedAnswer("answer1");
        Questions question2 = new Questions();
        question2.setSubmittedAnswer("answer2");
        submission.add(question1);
        submission.add(question2);
        // Set up the behavior of the questions service
        when(questionsService.isAnswerCorrect(question1)).thenReturn(true);
        when(questionsService.isAnswerCorrect(question2)).thenReturn(true);
        // Call the service method
        long score = quizService.submitQuiz(submission);
        // Verify the result
        assertEquals(2, score);
    }

    @Test
    public void testSubmitQuiz_WhenSomeAnswersAreIncorrect() {
        // Mock data
        List<Questions> submission = new ArrayList<>();
        Questions question1 = new Questions();
        question1.setSubmittedAnswer("answer1");
        Questions question2 = new Questions();
        question2.setSubmittedAnswer("answer2");
        submission.add(question1);
        submission.add(question2);
        // Set up the behavior of the questions service
        when(questionsService.isAnswerCorrect(question1)).thenReturn(true);
        when(questionsService.isAnswerCorrect(question2)).thenReturn(false);
        // Call the service method
        long score = quizService.submitQuiz(submission);
        // Verify the result
        assertEquals(1, score);
    }

    @Test
    public void testDeleteQuiz_WhenQuizExists() {
        // Mock data
        Long quizId = 1L;
        // Set up the behavior of the mock repository
        when(quizRepository.findByQuizId(quizId)).thenReturn(Optional.of(new Quiz()));
        // Call the service method
        quizService.deleteQuiz(quizId);
        // Verify that the deleteById method was called
        verify(quizRepository).deleteById(quizId);
    }

    @Test
    public void testDeleteQuiz_WhenQuizDoesNotExist() {
        // Mock data
        Long quizId = 1L;
        // Set up the behavior of the mock repository
        when(quizRepository.findByQuizId(quizId)).thenReturn(Optional.empty());
        // Call the service method and verify the exception
        assertThrows(ResourceNotFoundException.class, () -> quizService.deleteQuiz(quizId));
    }

    @Test
    public void testCheckIfQuizExists_WhenQuizExists() {
        // Mock data
        Long quizId = 1L;
        // Set up the behavior of the mock repository
        when(quizRepository.findByQuizId(quizId)).thenReturn(Optional.of(new Quiz()));
        // Call the service method
        boolean result = quizService.checkIfQuizExists(quizId);
        // Verify the result
        assertTrue(result);
    }

    @Test
    public void testCheckIfQuizExists_WhenQuizDoesNotExist() {
        // Mock data
        Long quizId = 1L;
        // Set up the behavior of the mock repository
        when(quizRepository.findByQuizId(quizId)).thenReturn(Optional.empty());
        // Call the service method and verify the exception
        assertThrows(ResourceNotFoundException.class, () -> quizService.checkIfQuizExists(quizId));
    }

    @Test
    public void testGetAdminStats() {
        // Mock data
        String authorization = "Bearer token";
        Long totalQuiz = 5L;
        Long totalSubmissions = 10L;
        List<Object> users = new ArrayList<>();
        users.add(new Object());
        users.add(new Object());
        // Set up the behavior of the mock repository and rest template
        when(quizRepository.count()).thenReturn(totalQuiz);
        when(submissionsService.getSubmissionsCount()).thenReturn(totalSubmissions);
        when(restTemplate.exchange(
                "http://USER-SERVICE/users/getall",
                HttpMethod.GET,
                new HttpEntity<>(createHeaders(authorization)),
                List.class)
        ).thenReturn(ResponseEntity.ok(users));
        // Call the service method
        Map<String, Long> stats = quizService.getAdminStats(authorization);
        // Verify the result
        assertEquals(totalQuiz, stats.get("quizCount"));
        assertEquals(totalSubmissions, stats.get("submissionsCount"));
        assertEquals(1L, stats.get("usersCount"));
    }

    @Test
    public void testGetUserStats() {
        // Mock data
        String authorization = "Bearer token";
        Integer userId = 1;
        Long totalSubmissions = 5L;
        // Set up the behavior of the submissions service
        when(submissionsService.getUserSubmissionsCount(userId)).thenReturn(totalSubmissions);
        // Call the service method
        Map<String, Long> stats = quizService.getUserStats(authorization, userId);
        // Verify the result
        assertEquals(totalSubmissions, stats.get("submissionsCount"));
    }

    private HttpHeaders createHeaders(String authorization) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authorization);
        return headers;
    }
}

