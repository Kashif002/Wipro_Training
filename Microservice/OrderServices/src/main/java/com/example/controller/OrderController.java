package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.beans.Order;
import com.example.repo.OrderRepo;
import com.example.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {
	@Autowired
	OrderService service;
	
	@Autowired
	OrderRepo repo;
	
	@GetMapping("{userId}")
	public List<Order> getUserById(@PathVariable("userId") Integer userId)
	{
		//
		return service.getOrderByUserById(userId);
		
	}
	
	@GetMapping("/all")
	public List<Order> getAllUsers()
	{
		return (List<Order>) repo.findAll();
	}
}
