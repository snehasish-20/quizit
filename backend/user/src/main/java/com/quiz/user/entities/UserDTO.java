package com.quiz.user.entities;

public class UserDTO {

	public int userId;
	
	public String name;
	
	public String email;
		
	public Role roleType;

	@Override
	public String toString() {
		return "UserDTO [userId=" + userId + ", name=" + name + ", email=" + email + ", roleType=" + roleType + "]";
	}

	public UserDTO(int userId, String name, String email, Role roleType) {
		super();
		this.userId = userId;
		this.name = name;
		this.email = email;
		this.roleType = roleType;
	}
	
	public UserDTO(User user) {
		super();
		this.userId = user.getUserId();
		this.name = user.getName();
		this.email = user.getEmail();
		this.roleType = user.getRole();
	}

	public UserDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getname() {
		return name;
	}

	public void setname(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Role getroleType() {
		return roleType;
	}

	public void setroleType(Role roleType) {
		this.roleType = roleType;
	}
}
