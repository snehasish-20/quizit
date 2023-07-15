package com.quiz.user.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quiz.user.entities.User;

public interface UserDao extends JpaRepository<User, Integer> {

	/**
	 * Finds an user by his email
	 * 
	 * @param email
	 * @return an USER object if found, else returns null
	 */
	Optional<User> findByEmail(String email);
}
