/** 
 * Name: Wenya Guo
 * Assignment: Java 3 Assignment 4
 * Program: Computer Programming
 * 
 * This project is for creating an application for a cheese factory.
 * In assignment4, we added the user registration form, and also moved the
 * database to a persistent storage system using MySQL server. 
 * 
 * This file is DatabaseConfig class
 *  
 * @author guowen
 * 
 */

package ca.sheridancollege.guowen.assign.database;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class DatabaseConfig {
	
	//create a @Bean method to give a JDBC template object
	@Bean
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource
			dataSource) {
		
		return new NamedParameterJdbcTemplate(dataSource);
	}

}
