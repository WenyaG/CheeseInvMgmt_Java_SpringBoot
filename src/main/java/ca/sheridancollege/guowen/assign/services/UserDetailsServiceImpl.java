/** 
 * Name: Wenya Guo
 * Assignment: Java 3 Assignment 4
 * Program: Computer Programming
 * 
 * This project is for creating an application for a cheese factory.
 * In assignment4, we added the user registration form, and also moved the
 * database to a persistent storage system using MySQL server. 
 * 
 * This file is a User Details Service to retrieve the information about user
 *  
 * @author guowen
 * 
 */

package ca.sheridancollege.guowen.assign.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ca.sheridancollege.guowen.assign.database.DatabaseAccess;




@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired @Lazy
	private DatabaseAccess da;

	@Override
	public UserDetails loadUserByUsername(String username) throws 
		UsernameNotFoundException {
		
		//find the user based on username
		ca.sheridancollege.guowen.assign.beans.User user = da.findUserAccount(username);
		
		//print user not found exception in console
		if (user == null) {
			System.out.printf("User not found: %s\n", username);
			throw new UsernameNotFoundException("User " + username + " not"
					+ "found in database.");
		}
		
		//gets a list of roles this user belongs to
		List<String> rolesList = da.getRolesByUserId(user.getUserId());	
		
		//convert our list of string role names into a list of granted 
		//authority objects
		List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
		if(rolesList != null) {
			for(String role : rolesList) {
				grantList.add(new SimpleGrantedAuthority(role));
			}
		}
		
		//creates a Userdetails user for the user that will be authenticated
		UserDetails userDetails = (UserDetails)(new User(user.getUserName(), 
				user.getPassword(),grantList));
		return userDetails;
		
	}

}
