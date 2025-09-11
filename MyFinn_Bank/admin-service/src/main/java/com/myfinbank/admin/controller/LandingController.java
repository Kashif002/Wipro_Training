package com.myfinbank.admin.controller;

import com.myfinbank.admin.entity.Admin;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LandingController {

    // Landing page accessible to all
	@GetMapping({"/", "/index", "/home"})
    public String showHomePage() {
        return "index"; // Your landing page template
    }

    // Login page - override the API endpoint with web controller
    @GetMapping("/login")
    public String showLoginPage() {
        return "admin/login"; // Returns login.html template
    }

    // Register page - override the API endpoint with web controller
    @GetMapping("/register") 
    public String showRegisterPage(Model model) {
        model.addAttribute("admin", new Admin());
        return "admin/register"; // Returns register.html template
    }

    // Handle registration form submission
    @PostMapping("/register")
    public String processRegistration(@ModelAttribute Admin admin, RedirectAttributes redirectAttrs) {
        // TODO: Implement actual registration logic in AdminService
        redirectAttrs.addFlashAttribute("message", "Registration successful! Please login.");
        return "redirect:/login";
    }
}
