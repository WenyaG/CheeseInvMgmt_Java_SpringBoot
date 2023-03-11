/** 
 * Name: Wenya Guo
 * Assignment: Java 3 Assignment 4
 * Program: Computer Programming
 * 
 * This project is for creating an application for a cheese factory.
 * In assignment4, we added the user registration form, and also moved the
 * database to a persistent storage system using MySQL server. 
 * 
 * This file is SecurityConfig class to deal with the security settings.
 *  
 * @author guowen
 * 
 */

package ca.sheridancollege.guowen.assign.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import ca.sheridancollege.guowen.assign.services.UserDetailsServiceImpl;


@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure (HttpSecurity http) throws Exception {
		//TODO: remove or comment out after debugging
				//http.csrf().disable()//disables protection from cross site request forgery
					//.headers().frameOptions().disable();
				
				http.authorizeHttpRequests()
				
					//only MANAGER can access /inventory/**
					.antMatchers("/inventory/**").hasRole("MANAGER")
					
					
					//anyone can access any of these resources
					.antMatchers("/", "/login", "/register","/view", 
							"/css/**", "/images/**", "/scripts/**")
					.permitAll()
					
					
					//TODO: disable this after debugging
					//.antMatchers("/h2-console/**").permitAll()
					
					//only CUSTOMER can access
					.anyRequest().authenticated() 
				.and()
				//configure how form authentication works
				.formLogin().loginPage("/login") //location of login page
					.permitAll()
				.and() 
				//configure logout
				.logout()
					.invalidateHttpSession(true) //destroy the session on logout
					.clearAuthentication(true) // clear authentication data
					//the url trigger logout
					.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
					//the url when logout successfully
					.logoutSuccessUrl("/login?logout")
					.permitAll();  //anyone can access those urls
	}
	
	
	//configures the user authentication for this application to use our 
	//userDetailsService instance. 
	//hash the user-entered password and match it to the hash value in the 
	//users table for this user
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.userDetailsService(userDetailsService)
			.passwordEncoder(passwordEncoder());
	
	}

}
