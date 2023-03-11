/** 
 * Name: Wenya Guo
 * Assignment: Java 3 Assignment 4
 * Program: Computer Programming
 * 
 * This project is for creating an application for a cheese factory.
 * In assignment4, we added the user registration form, and also moved the
 * database to a persistent storage system using MySQL server. 
 * 
 * This file is a java bean of User class 
 * 
 * @author guowen
 * 
 */

package ca.sheridancollege.guowen.assign.beans;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * User class with lombok, created for user registration 
 * @author guowe
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Data
public class User implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private long userId;
	@NonNull 
	private String userName;
	private String email;
	@NonNull 
	private String password;
	private Boolean enabled;
	

}
