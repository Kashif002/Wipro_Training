package com.wipro.springcore.annotationbasedconfig;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.wipro.springcore.annotationbeans.Customer;

@Configuration 
@ComponentScan(basePackages = "com.wipro.springcore.xmlbasedconfig,com.wipro.springcore.annotationbeans")
public class AppConfig {
	public Customer customer()
	{
		return new Customer();
	}
}