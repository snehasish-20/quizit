package com.quizapp.quiz.config;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import com.quizapp.quiz.controllers.QuizController;
import com.quizapp.quiz.services.AuthenticationService;
import com.quizapp.quiz.services.QuizService;
import com.quizapp.quiz.services.SubmissionsService;

@TestConfiguration
@Import({ QuizController.class })
@ComponentScan("com.quizapp.quiz.services")

public class QuizControllerTestConfig {

    @Bean
    public QuizService quizService() {
        return Mockito.mock(QuizService.class);
    }

    @Bean
    public SubmissionsService submissionsService() {
        return Mockito.mock(SubmissionsService.class);
    }

    @Bean
    public AuthenticationService authenticationService() {
        return Mockito.mock(AuthenticationService.class);
    }
}

