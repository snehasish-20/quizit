package com.quiz.user.config;
import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.quiz.user.dao.UserDao;


@Configuration
public class UserConfig {
	
	@Autowired
	@Lazy
	private AuthenticationProvider authenticationProvider;
	
	@Autowired
	@Lazy
	private JwtAuthenticationFilter jwtAuthFilter;
	
	@Autowired
	private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
	
	@Autowired
	private UserDao repo;

	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/users/token", "/users/login", "/users/validate", "/users/register").permitAll()
                                .anyRequest().authenticated()
                )
                .csrf(c -> c.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .httpBasic(withDefaults())
                .build();
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		return username -> repo.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}