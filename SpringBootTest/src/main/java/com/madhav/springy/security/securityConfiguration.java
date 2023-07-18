package com.madhav.springy.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class securityConfiguration {

	/*
	 * this is a default database provided by userDetailsService by which spring
	 * security provides us a ready-made database for storing user data without you
	 * needing to create the table for one
	 */

	/*
	 * create a new instance of UserDetailsManager which will handle the data passed
	 * to securityFilterChain which authenticate the data
	 */

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/*
	 * This is simply non-production way of testing spring security , do not use it
	 * for actual projects
	 */

	@Bean
	InMemoryUserDetailsManager userDetailsService() {
		String encodedPassword = passwordEncoder().encode("password");
		UserDetails user = User.withUsername("user").password(encodedPassword).roles("USER")
				.build();
		UserDetails admin = User.withUsername("admin").password(encodedPassword).roles("ADMIN")
				.build();
		return new InMemoryUserDetailsManager(user, admin);
	}

	@Bean
	SecurityFilterChain configure(HttpSecurity http) throws Exception {
		return http.authorizeHttpRequests(auth -> {
			auth.requestMatchers("/user").hasRole("USER")
					.requestMatchers("/admin").hasRole("ADMIN").anyRequest().permitAll();
		}).formLogin(Customizer.withDefaults()).build();
	}

}
