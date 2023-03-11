/** 
 * Name: Wenya Guo
 * Assignment: Java 3 Assignment 4
 * Program: Computer Programming
 * 
 * This project is for creating an application for a cheese factory.
 * In assignment4, we added the user registration form, and also moved the
 * database to a persistent storage system using MySQL server. 
 * 
 * This file is DatabaseAccess class for operating database
 *  
 * @author guowen
 * 
 */

package ca.sheridancollege.guowen.assign.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.guowen.assign.beans.Cheese;
import ca.sheridancollege.guowen.assign.beans.Units;
import ca.sheridancollege.guowen.assign.beans.User;




@Repository
public class DatabaseAccess {
	
	@Autowired
	private NamedParameterJdbcTemplate jdbc;
	@Autowired
	private BCryptPasswordEncoder encoder;

	
	/**
	 * Method to return a list of Unit objects in table "units"
	 * @return a list of Units Objects
	 */
	public List<Units> getUnits() {
		
		String sql = "SELECT * FROM units;";
		List<Units> units = jdbc.query(sql, new BeanPropertyRowMapper<Units>
				(Units.class));
		
		return units;
	}
	
	
	/**
	 * Method to return a Map of Units to display the units name according to id
	 * @return a map of Units
	 */
	public Map<Integer, Units> getUnitsMap() {
		List<Units> units = getUnits();
		Map<Integer, Units> unitsMap = new HashMap<>();
		for (Units u : units) {
			unitsMap.put(u.getId(), u);
		}
		
		return unitsMap;
	}
	
	
	/**
	 * Method to return a list of Cheese objects according to cheeses table, 
	 * sorted by cheese name and unit id
	 * @return a list of Cheese objects
	 */
	public List<Cheese> getCheeseInventory() {
		String sql = "SELECT * FROM cheeses ORDER BY name, id;";
		List<Cheese> cheeses = jdbc.query(sql, 
				new BeanPropertyRowMapper<Cheese>(Cheese.class));
		return cheeses;
	}
	
	/**
	 * Method to insert a new Cheese object into "cheeses" table
	 * @param object of Cheese class
	 * @return the output from the INSERT query
	 */
	public int insertCheese(Cheese cheese) {
		
		String sql = "INSERT INTO cheeses (name, quantity, weight, unitsId,"
				+ "price, specSheet) VALUES (:name, :quantity, :weight, "
				+ ":unitsId, :price, :specSheet);"; 
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("name", cheese.getName())
		      .addValue("quantity", cheese.getQuantity())
		      .addValue("weight", cheese.getWeight())
		      .addValue("unitsId", cheese.getUnitsId())
		      .addValue("price", cheese.getPrice())
		      .addValue("specSheet", cheese.getSpecSheet());
		return jdbc.update(sql, params);
	}
	
	
	/**
	 * Method to list distinct cheese records in "cheeses" table
	 * @return a list of distinct objects of Cheese
	 */
	public List<Cheese> getCheeses() {
		String sql = "SELECT DISTINCT name, unitsId, weight FROM cheeses;";
		List<Cheese> distinctCheeses = jdbc.query(sql, 
				new BeanPropertyRowMapper<Cheese>(Cheese.class));
		return distinctCheeses;
	}
	
	/**
	 * Method to update existing record in cheeses table according to a 
	 * new cheese object
	 * @param object of Cheese class
	 * @return the output from the UPDATE query
	 */
	public int updateCheese(Cheese cheese) {
		
		String sql = "UPDATE cheeses SET quantity=quantity+:qty, "
				+ "weight=:weight, price=:price,"
				+ "specSheet=:specSheet WHERE name=:name AND unitsId=:unitsId;";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("name", cheese.getName())
			.addValue("unitsId", cheese.getUnitsId())
			.addValue("qty", cheese.getQuantity())
			.addValue("weight", cheese.getWeight())
			.addValue("price", cheese.getPrice())
			.addValue("specSheet", cheese.getSpecSheet());
		
		return jdbc.update(sql, params);
		
		
	}
	
	
	/**
	 * Methods to match existing cheese records in database with a specific
	 * name and unitsId
	 * @param a new cheese name
	 * @param a new unitsId
	 * @return a selected cheese object which has same name and same unitsId, 
	 * 		   return null if no matched record
	 */
	public Cheese matchCheese(String name, int unitsId) {
		String sql = "SELECT * FROM cheeses WHERE name=:name and "
				+ "unitsId=:unitsId";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("name", name)
			.addValue("unitsId", unitsId);
		
		List<Cheese> newCheeseList = jdbc.query(sql, params, 
				new BeanPropertyRowMapper<Cheese>(Cheese.class));
    	return newCheeseList.isEmpty() ? null : newCheeseList.get(0);
	}
	
	
	/**
	 * Method to find if userName has been exist in users table
	 * @param a userName
	 * @return if this username exists, it returns this user. If not, it returns 
	 * null
	 */
	public User findUserAccount(String userName) {
		String sql = "SELECT * FROM users WHERE userName=:userName;";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("userName", userName);
		List<User> results = jdbc.query(sql, params,
				new BeanPropertyRowMapper<User>(User.class));
		if(results.size() > 0)
			return results.get(0);
		else
			return null;
		
	}
	
	/**
	 * Method to use the userId to map the role
	 * @param a userId
	 * @return a list of roles which the selected user has
	 */
	public List<String> getRolesByUserId(long userId) {
		String sql = "SELECT user_role.userid, roles.rolename FROM user_role,"
				+ "roles WHERE user_role.roleid = roles.roleid AND "
				+ "user_role.userid = :userid;";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("userid", userId);
		List<String> roles = new ArrayList<String>();	
		
		List<Map<String, Object>> results = jdbc.queryForList(sql, params);
		for(Map<String, Object> row : results) {
			roles.add((String)row.get("rolename"));
			
		}
		
		return roles;
	}
	
	/**
	 * Method to add a new user
	 * @param userName
	 * @param email
	 * @param password
	 * @return the primary key value that generated 
	 */
	public long addUser(User user) {
		String sql = "INSERT INTO users (userName, email, password, enabled)"
				+ "VALUES (:userName, :email, :pass, true);";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("userName", user.getUserName())
		      .addValue("email", user.getEmail())
			  .addValue("pass", encoder.encode(user.getPassword()));
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbc.update(sql, params, keyHolder);
		return keyHolder.getKey().longValue(); 
	}
	
	
	/**
	 * Method to add a record in user_role table for specific user and role
	 * @param a userId
	 * @param a roleId
	 * @return the output from the INSERT
	 */
	public int addUserRole(long userId, long roleId) {
		String sql = "INSERT INTO user_role (userid, roleid) VALUES (:user, :role);";
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("user", userId).addValue("role", roleId);
		return jdbc.update(sql, params);

	}
	
	
	/**
	 * Method to update user email and password by userId
	 * @param email
	 * @param password
	 * @return  the output from the UPDATE query
	 */
	public long updateUser(User user) {
		String sql = "UPDATE users SET email=:email, password=:pass WHERE "
				+ "userId=:userId;";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("userId", user.getUserId())
			  .addValue("email", user.getEmail())
			  .addValue("pass", encoder.encode(user.getPassword()));

		return jdbc.update(sql, params);
	}
	
	

}
