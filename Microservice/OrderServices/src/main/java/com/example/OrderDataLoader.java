package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.beans.Order;
import com.example.repo.OrderRepo;

import jakarta.annotation.PostConstruct;

@Component
public class OrderDataLoader {
	@Autowired
	OrderRepo repo;
	
	@Autowired
	public OrderDataLoader(OrderRepo repo) {
		super();
		this.repo = repo;
	}

	
   @PostConstruct
	public void loadDummyData()
	{
		
		 Order order1 = new Order(1,"Laptop" , 60000 , 1, 1);
	   
		 Order order2 = new Order(2,"Mobile" , 18850 , 1, 1);
		
		 Order order3 = new Order(3, "Tablet" , 25000 , 1 , 1);
		
		 Order order4 = new Order(4,"Laptop" , 60000 , 2, 2);
		   
		 Order order5 = new Order(5,"Mobile" , 52634 , 1, 2);
		
		 Order order6 = new Order(6,"Earphones" , 2600 , 2, 3);
		
		 Order order7 = new Order(7,"Microwave" , 9000 , 1, 3);
	
		 Order order8 = new Order(8,"Toaster" , 6500 , 2, 4);
		
		 Order order9 = new Order(9,"LCD" , 52000 , 1, 4);
		
		repo.save(order1);
		repo.save(order2);
		repo.save(order3);
		repo.save(order4);
		repo.save(order5);
		repo.save(order6);
		repo.save(order7);
		repo.save(order8);
		repo.save(order9);
		
		
		System.out.println("Data loaded safely");
}
}
