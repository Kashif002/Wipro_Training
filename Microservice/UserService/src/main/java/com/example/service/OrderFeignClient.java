package com.example.service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.beans.Contact;
import com.example.beans.Order;

@FeignClient(name ="order-ms" , url="http://localhost:8084")
public interface OrderFeignClient {
	
	@GetMapping("/order/{userId}")
	List<Order> getSpecificOrder(@PathVariable("userId") int userId);

	
}
