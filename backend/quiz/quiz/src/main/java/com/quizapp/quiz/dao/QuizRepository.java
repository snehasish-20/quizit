package com.quizapp.quiz.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quizapp.quiz.entities.Quiz;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

	public Optional<Quiz> findByQuizId(Long quizId);
	
}
