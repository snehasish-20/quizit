package com.quizapp.quiz.services;

import java.util.HashMap;
import java.util.List;

import com.quizapp.quiz.entities.Submissions;

public interface SubmissionsService {

	public Submissions submitQuizResults(Submissions submission);
	public List<Submissions> getSubmissions(Long quizId, String authorization);
	public Submissions getSubmissionsByQuizAndUserId(long quizId, long userId);
	public Long getSubmissionsCount();
	public Long getUserSubmissionsCount(Integer userId);
	public List<HashMap<String,String>> getSubmissionsByUserId(Integer UserId);
}
