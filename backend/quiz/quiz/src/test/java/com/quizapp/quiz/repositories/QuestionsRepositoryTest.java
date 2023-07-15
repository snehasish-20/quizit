package com.quizapp.quiz.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.quizapp.quiz.dao.QuestionsRepository;
import com.quizapp.quiz.dao.QuizRepository;
import com.quizapp.quiz.entities.Questions;
import com.quizapp.quiz.entities.Quiz;

@SpringBootTest
class QuestionsRepositoryTest {
  
  @Autowired
  private QuestionsRepository questionsRepositoryobj;
  
  @Autowired
  private QuizRepository quizRepository;
  
  Quiz quiz;
  
  Questions question;
  
  
//  @Query(value="select q.question_id, q.question, q.option1, q.option2, q.option3, q.option4, q.answer from questions as q where q.quiz_id = :quizId", nativeQuery = true)
//	public List<Questions> getAllWithAnswers(@Param("quizId") Long quizId);
//	
//	@Query(value="select q.question_id, q.question, q.option1, q.option2, q.option3, q.option4 from questions as q where q.quiz_id = :quizId", nativeQuery = true)
//	public List<Questions> getAllWithoutAnswers(@Param("quizId") Long quizId);
//
//	public Questions findByQuestionId(Long entry);
	
	@BeforeEach
	void setUp() {
		List<Questions> questions= new ArrayList<>();
		question = new Questions(1, "test 1", "1", "2", "3", "4", "4", null);
		questions.add(question);
		quiz = new Quiz(1, "test quiz", true, 1, questions, null);
		quiz = quizRepository.save(quiz);
	}
	
	@AfterEach
	void tearDown() {
		quizRepository.deleteAll();
		questionsRepositoryobj.deleteAll();
	}
	
	@Test
	void testfindByQuestionId() {
		Questions q = questionsRepositoryobj.findByQuestionId(quiz.getQuestions().get(0).getQuestionId());
		assertThat(q.getQuestion()).isEqualTo(question.getQuestion());
	}
	
	@Test
	void getAllWithAnswers() {
		List<Questions> q = questionsRepositoryobj.getAllWithAnswers(quiz.getQuizId());
		assertThat(q.get(0).getAnswer()).isEqualTo(question.getAnswer());
	}
  
}

