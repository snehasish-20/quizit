package com.quizapp.quiz.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.quizapp.quiz.dao.QuestionsRepository;
import com.quizapp.quiz.entities.Questions;

@SpringBootTest
public class QuestionsServiceImplTest {

    @Mock
    private QuestionsRepository questionsRepository;

    private QuestionsServiceImpl questionsService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        questionsService = new QuestionsServiceImpl();
        questionsService.setQuestionsRepository(questionsRepository);
    }

    @Test
    public void testGetQuestionsWithAnswers() {
        // Mock data
        Long quizId = 1L;
        List<Questions> expectedQuestions = new ArrayList<>();
        // Set up the behavior of the mock repository
        when(questionsRepository.getAllWithAnswers(quizId)).thenReturn(expectedQuestions);
        // Call the service method
        List<Questions> actualQuestions = questionsService.getQuestionsWithAnswers(quizId);
        // Verify the result
        assertEquals(expectedQuestions, actualQuestions);
    }

    @Test
    public void testCreateQuestions() {
        // Mock data
        List<Questions> questions = new ArrayList<>();
        List<Questions> expectedQuestions = new ArrayList<>();
        // Set up the behavior of the mock repository
        when(questionsRepository.saveAll(questions)).thenReturn(expectedQuestions);
        // Call the service method
        List<Questions> actualQuestions = questionsService.createQuestions(questions);
        // Verify the result
        assertEquals(expectedQuestions, actualQuestions);
    }

    @Test
    public void testGetQuestionsWithoutAnswers() {
        // Mock data
        Long quizId = 1L;
        List<Questions> expectedQuestions = new ArrayList<>();
        // Set up the behavior of the mock repository
        when(questionsRepository.getAllWithoutAnswers(quizId)).thenReturn(expectedQuestions);
        // Call the service method
        List<Questions> actualQuestions = questionsService.getQuestionsWithoutAnswers(quizId);
        // Verify the result
        assertEquals(expectedQuestions, actualQuestions);
    }

    @Test
    public void testIsAnswerCorrect_WhenAnswerIsCorrect() {
        // Mock data
        Questions question = new Questions();
        question.setQuestionId(1L);
        question.setAnswer("correctAnswer");
        Questions entry = new Questions();
        entry.setQuestionId(1L);
        entry.setSubmittedAnswer("correctAnswer");
        // Set up the behavior of the mock repository
        when(questionsRepository.findByQuestionId(1L)).thenReturn(question);
        // Call the service method
        boolean isCorrect = questionsService.isAnswerCorrect(entry);
        // Verify the result
        assertTrue(isCorrect);
    }

    @Test
    public void testIsAnswerCorrect_WhenAnswerIsIncorrect() {
        // Mock data
        Questions question = new Questions();
        question.setQuestionId(1L);
        question.setAnswer("correctAnswer");
        Questions entry = new Questions();
        entry.setQuestionId(1L);
        entry.setSubmittedAnswer("incorrectAnswer");
        // Set up the behavior of the mock repository
        when(questionsRepository.findByQuestionId(1L)).thenReturn(question);
        // Call the service method
        boolean isCorrect = questionsService.isAnswerCorrect(entry);
        // Verify the result
        assertFalse(isCorrect);
    }

    @Test
    public void testFilterQuestionsForUser() {
        // Mock data
        List<Questions> questions = new ArrayList<>();
        Questions question1 = new Questions();
        question1.setAnswer("answer1");
        Questions question2 = new Questions();
        question2.setAnswer("answer2");
        questions.add(question1);
        questions.add(question2);
        List<Questions> expectedQuestions = new ArrayList<>();
        expectedQuestions.add(question1);
        expectedQuestions.add(question2);
        // Call the service method
        List<Questions> actualQuestions = questionsService.filterQuestionsForUser(questions);
        // Verify the result
        for (Questions actualQuestion : actualQuestions) {
            assertNull(actualQuestion.getAnswer());
        }
        assertEquals(expectedQuestions, actualQuestions);
    }
}
