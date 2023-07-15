package com.quiz.user.entities;

public class LoginResponse {

	private String status;
	private String message;
	private String token;
	
	private UserDTO user;

	public LoginResponse(String status, String message, String token, UserDTO user) {
		super();
		this.status = status;
		this.message = message;
		this.user = user;
		this.token = token;
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public LoginResponse() {
		super();
		this.status = "Error";
		this.message = "Incorrect password";
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}
	
	
}
