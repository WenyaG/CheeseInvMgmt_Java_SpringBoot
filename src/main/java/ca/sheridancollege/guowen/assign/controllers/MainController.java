/** 
 * Name: Wenya Guo
 * Assignment: Java 3 Assignment 4
 * Program: Computer Programming
 * 
 * This project is for creating an application for a cheese factory.
 * In assignment4, we added the user registration form, and also moved the
 * database to a persistent storage system using MySQL server. 
 * 
 * This file is a main controller to handle the request to load the web pages
 *  
 * @author guowen
 * 
 */

package ca.sheridancollege.guowen.assign.controllers;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sheridancollege.guowen.assign.beans.Cheese;
import ca.sheridancollege.guowen.assign.beans.User;
import ca.sheridancollege.guowen.assign.database.DatabaseAccess;



@Controller
public class MainController {
	
	
	@Autowired @Lazy
	private DatabaseAccess da;
	
	/**
	 * Handler to load the home index page.
	 * Match the required url: localhost:8080/ or 
	 * localhost:8080/index.html or localhost:8080/index.htm
	 * Add units and unitsMap to session to limit the number of database access
	 * @param session, model
	 * @return home page
	 */
	@GetMapping(value = {"/","/index.htm","/index.html"})
	public String home(HttpSession session, Model model) {
		
		if (session.getAttribute("units") == null) {
			session.setAttribute("units", da.getUnits());
		};
		if (session.getAttribute("unitsMap") == null) {
			session.setAttribute("unitsMap", da.getUnitsMap());
		};

		return "index.html";
	}

	
	/**
	 * Handler to load the add cheese inventory form page
	 * This page is only for MANAGER role users. 
	 * @param model
	 * @return index page under /inventory directory
	 */
	@GetMapping("/inventory/new")
	public String newForm(Model model) {
		
		//add an empty cheese object for form binding
		model.addAttribute("cheese", new Cheese());
	
		return "/inventory/index.html";
	}
	
	/**
	 * Handler to process the form. 
	 * This page is only for MANAGER role users. 
	 * @param model
	 * @param cheese object which constructed from the form input
	 * @return index page under /inventory directory
	 */
	@PostMapping("/inventory/addcheese")
	public String addCheese(Model model, 
			@ModelAttribute Cheese cheese) {
		
		//Select the object with the same input name and unitsId from database 
		Cheese c = da.matchCheese(cheese.getName(), cheese.getUnitsId());
		
		//check if a record already exists for a specific unit of cheese. 
		//If it exists, add the new quantity to the existing quantity and update
		//price, weight and url. 
		//If it doesn't exist, add it as a new record.
		if (c == null) {
			da.insertCheese(cheese);
		} else {
			da.updateCheese(cheese);
		}
		
		//add a list of Cheese objects according to updated cheeses table
		//into key "cheeseInv" 
		model.addAttribute("cheeseInv", da.getCheeseInventory());
		return "/inventory/view.html";
	
	}
	
	
	/**
	 * Handler for "view out cheese page"
	 * @param model, session
	 * @return view.html under the root directory
	 * 
	 * Add session here for loading units and unitsMap, then user can enter the
	 * view page after log out. 
	 */
	@GetMapping("/view")
	public String viewCheese(HttpSession session, Model model) {
		

		//add a List of Cheese objects for distinct records into "cheeses"
		model.addAttribute("cheeses", da.getCheeses());
		
		if (session.getAttribute("units") == null) {
			session.setAttribute("units", da.getUnits());
		};
		if (session.getAttribute("unitsMap") == null) {
			session.setAttribute("unitsMap", da.getUnitsMap());
		};
		
		return "view.html";
	}
	
	/**
	 * Handler for "view cheese inventory" page. 
	 * This page is only displaying to MANAGER role users. 
	 * @param model
	 * @return view.html under /inventory directory
	 */
	@GetMapping("/inventory/view")
	public String viewInventory(Model model) {

		//add a list of Cheese objects according to updated cheeses table
		//into key "cheeseInv" 
		model.addAttribute("cheeseInv", da.getCheeseInventory());

		
		return "/inventory/view.html";
	}
	
	
	/**
	 * Handler for login form page. 
	 * @param model
	 * @return login.html
	 */
	@GetMapping("/login")
	public String loginForm() {
		return "login.html";
	}
	
	/**
	 * Handler for register page
	 * @return register.html
	 */
	@GetMapping("/register")
	public String loadRegisterForm(Model model) {
		model.addAttribute("user", new User());
		return "register.html";
	}
	
	
	/**
	 * Handler for processing new user register
	 * @param username
	 * @param email
	 * @param password
	 * @return redirect to /login
	 */
	@PostMapping ("/register")
	public String registerUser(@ModelAttribute User user) {
		if (user.getUserId() > 0) {
			da.updateUser(user);
		} else {
			long userId = da.addUser(user);
			da.addUserRole(userId, 2);
		}
		return "redirect:/login";
		
	}
	
	/**
	 * Handler for editing user profile, direct to register page.
	 * @param current logged in user
	 * @param model
	 * @return  register page. Reuse this page for editing email and password
	 */
	@GetMapping("/edit")
	public String editProfile(Principal principal, Model model) {
		User user = da.findUserAccount(principal.getName());
		model.addAttribute("user", user);
		return "register.html";
	}

}
