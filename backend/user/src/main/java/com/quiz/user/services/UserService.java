package com.quiz.user.services;

import java.util.List;

import com.quiz.user.entities.Login;
import com.quiz.user.entities.LoginResponse;
import com.quiz.user.entities.User;
import com.quiz.user.entities.UserDTO;

public interface UserService {

	public List<UserDTO> getUsers();
	public UserDTO getUsers(int userId);
	public UserDTO registerUser(User user);
	public LoginResponse loginUser(Login user);
	public User getUserDetails(String email);

}
