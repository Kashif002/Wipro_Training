package com.wipro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.wipro.entities.User;
import com.wipro.services.UserService;

import javax.servlet.http.HttpSession;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username,
                          @RequestParam String password,
                          Model model,
                          HttpSession session) {

        User user = userService.getByEmail(username);
        if (user == null) {
            user = userService.getByName(username);
        }

        if (user != null && user.getPassword().equals(password)) {
            // Store user in session
            session.setAttribute("user", user);
            
            if ("ADMIN".equals(user.getRole())) {
                return "redirect:/admin/dashboard";
            } else if ("USER".equals(user.getRole())) {
                return "redirect:/user/dashboard";
            } else {
                model.addAttribute("error", "Invalid role!");
                return "login";
            }
        } else {
            model.addAttribute("error", "Invalid username/email or password!");
            return "login";
        }
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@RequestParam String username,
                             @RequestParam String email,
                             @RequestParam String password,
                             @RequestParam String role,
                             Model model) {
        // Check if user already exists
        if (userService.getByEmail(email) != null) {
            model.addAttribute("error", "Email already registered!");
            return "register";
        }
        
        if (userService.getByName(username) != null) {
            model.addAttribute("error", "Username already taken!");
            return "register";
        }

        // Create and save user
        User newUser = new User(username, email, password, role);
        userService.register(newUser);
        model.addAttribute("message", "User registered successfully! Please login.");
        return "login";
    }
    
    // Add logout functionality
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}