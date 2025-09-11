package com.gl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gl.web.User;
import com.gl.web.UserDao;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserDao dao;

	@RequestMapping(method = RequestMethod.GET)
	public String getUser(Model model) {
	    model.addAttribute("msg", "enter your details");
	    model.addAttribute("user", new User()); // Add User object for form binding
	    return "Signin";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String loginUser(Model model, @ModelAttribute("user") User user) {
	    if (user.getEmail() != null && !user.getEmail().isEmpty() && 
	        user.getPassword() != null && !user.getPassword().isEmpty()) {
	        
	        dao.saveUser(user);
	        model.addAttribute("msg", user.getName());
	        return "success";
	    } else {
	        model.addAttribute("error", "User details can't be empty");
	        return "Signin";
	    }
	}
	
	@RequestMapping(value="/getall", method = RequestMethod.GET)
	public String getAllUsers(Model model) {
	    List<User> list = dao.getUsers();
	    model.addAttribute("userlist", list); // Pass List<User> directly
	    return "homeuser";
	}
	
}
