package com.wipro.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

	@GetMapping("/")
	public String WelcomeMessage()
	{
		return "Welcome to Spring Security Session";
	}
}