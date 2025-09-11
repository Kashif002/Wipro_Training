package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.beans.Order;
import com.example.repo.OrderRepo;

@Service
public class OrderService {

	@Autowired
OrderRepo repo;
	
	public List<Order> getOrderByUserById(Integer userId)
	{
		
		return repo.findByUserId(userId);
	}
}
