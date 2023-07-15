//package com.quizapp.quiz.repositories;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import java.util.List;
//import java.util.Optional;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import com.quizapp.quiz.dao.QuizRepository;
//import com.quizapp.quiz.entities.Quiz;
//
//@SpringBootTest
//public class QuizRepositoryTest {
//
//	@Autowired
//	private QuizRepository quizRepo;
//	Quiz quiz;
//
//	@Before
//	void setUp() {
//		quiz = new Quiz(2, "javaQuiz12222", true, 110, null, null);
//		quizRepo.save(quiz);
//	}
//	
//	@After
//	void tearDown() {
//		quizRepo.deleteAll();
//	}
//	
//	@Test
//	public void testFindAll() {
//		List<Quiz> res= quizRepo.findAll();
//		assertThat(res.get(1).getQuizName()).isEqualTo(quiz.getQuizName());
//	}
//	
////	@Test
////	public void testFindByQuizId() {
////		Optional<Quiz> quiz = quizRepo.findByQuizId(quiz.getQuizId());
////		assertThat(quiz.getQuizName()).isEqualTo(quiz.getQuizName());
////	}
//
//}
