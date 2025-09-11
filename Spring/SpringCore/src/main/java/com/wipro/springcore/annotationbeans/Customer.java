package com.wipro.springcore.annotationbeans;

import org.springframework.stereotype.Component;
import java.util.Scanner;

@Component
public class Customer {
	
	private int id;
	private String name;
	private int rating;
	
	Scanner sc=new Scanner(System.in);
	
	public Customer() {
		System.out.print("Enter customer ID: ");
	    this.id = sc.nextInt();
	    
	    System.out.print("Enter customer Name: ");
	    this.name = sc.next();

	    System.out.print("Enter customer Rating: ");
	    this.rating = sc.nextInt();
	   
	}

	public void welcomeCustomer()
	{
		
		System.out.println("Annotation Based Config File");
		System.out.println("Customer Details:");
		System.out.println("ID: " + id);
		System.out.println("Name: " + name);
		System.out.println("Rating: " + rating);
	}
	
}