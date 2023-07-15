package com.quizapp.quiz.services;

import java.util.List;

import com.quizapp.quiz.entities.Questions;

public interface QuestionsService {

	public List<Questions> getQuestionsWithAnswers(Long quizId); 
	public List<Questions> getQuestionsWithoutAnswers(Long quizId);
	public List<Questions> createQuestions(List<Questions> questions);
	public boolean isAnswerCorrect(Questions entry); 
	public List<Questions> filterQuestionsForUser(List<Questions> questions);
}
