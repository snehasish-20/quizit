package com.quiz.user.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quiz.user.entities.Login;
import com.quiz.user.entities.LoginResponse;
import com.quiz.user.entities.Role;
import com.quiz.user.entities.User;
import com.quiz.user.entities.UserDTO;
import com.quiz.user.exceptions.AuthHeaderNotFoundException;
import com.quiz.user.services.JwtService;
import com.quiz.user.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class userController {

	@Autowired
	private UserService UserServiceObj;

	@Autowired
	private JwtService jwtService;

	/**
	 * Retrieves the details of all the users. This method handles the HTTP GET
	 * request to the "/users/getall" endpoint. It fetches and returns a list of
	 * UserDTO objects containing the details of all the users in the system.
	 * 
	 * @return a list of UserDTO objects representing the details of all the users
	 * 
	 */
	@GetMapping("/getall")
	public List<UserDTO> getUsers() {
		return this.UserServiceObj.getUsers();
	}

	/**
	 * 
	 * Retrieves the details of a single user by their user ID. This method handles
	 * the HTTP GET request to the "/users" endpoint.
	 * 
	 * @param userId the ID of the user to retrieve
	 * @return a UserDTO object representing the details of the user with the
	 *         specified user ID
	 */
	@GetMapping("")
	public UserDTO getUserById(@RequestParam Integer userId) {
		return this.UserServiceObj.getUsers(userId);
	}

	/**
	 * 
	 * Registers a new user in the system. This method handles the HTTP POST request
	 * to the "/users/register" endpoint. It takes a User object as input, sets the
	 * role as "USER", and registers the user in the system. The registered user's
	 * details are returned as a UserDTO object.
	 * 
	 * @param user the User object representing the user to be registered
	 * @return a UserDTO object containing the details of the registered user
	 */
	@PostMapping("/register")
	public UserDTO registerUser(@Valid @RequestBody User user) {
		user.setRole(Role.USER);
		return this.UserServiceObj.registerUser(user);
	}

	/**
	 * 
	 * Authenticates a user and generates a JWT token upon successful login. This
	 * method handles the HTTP POST request to the "/users/login" endpoint. It takes
	 * a Login object as input, which contains the user's credentials (email and
	 * password) for authentication. Upon successful authentication, a LoginResponse
	 * object is generated containing the JWT token. The response is returned with a
	 * ResponseEntity containing the LoginResponse object and an "Authorization"
	 * header with the JWT token.
	 * 
	 * @param user the Login object containing the user's credentials for
	 *             authentication
	 * @return a ResponseEntity containing the LoginResponse object and an
	 *         "Authorization" header with the JWT token
	 */

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody Login user) {
		LoginResponse response = this.UserServiceObj.loginUser(user);
		return ResponseEntity.status(HttpStatus.OK).header("Authorization", "Bearer " + response.getToken())
				.body(response);
	}

	/**
	 * This method acts as the middleware
	 * 
	 * @param authHeader
	 * @return User object
	 */
	private User getUserFromAuthHeader(String authHeader) {
		String email = getEmailFromAuthHeader(authHeader);
		return UserServiceObj.getUserDetails(email);
	}

	/**
	 * This method decodes the email id from the authHeader string
	 * 
	 * @param authHeader
	 * @return email
	 */
	private String getEmailFromAuthHeader(String authHeader) {
		if (!authHeader.contains("Bearer "))
			throw new AuthHeaderNotFoundException();
		String email = jwtService.extractUsername(authHeader.substring(7));
		return email;
	}

	/**
	 * 
	 * Validates a JWT token and retrieves the corresponding user details. This
	 * method handles the HTTP GET request to the "/users/token/validate" endpoint.
	 * It takes the "authorization" header as input, which contains the JWT token.
	 * The method validates the token, extracts the user email from the token, and
	 * retrieves the user details using the email. The user details are returned as
	 * a User object. Note: This method acts as middleware to validate and retrieve
	 * user details based on the JWT token.
	 * 
	 * @param authorization the "Authorization" header containing the JWT token
	 * @return a User object representing the user details corresponding to the JWT
	 *         token
	 */

	@GetMapping("/token/validate")
	public User validateToken(@RequestHeader String authorization) {
		return getUserFromAuthHeader(authorization);
	}
}
