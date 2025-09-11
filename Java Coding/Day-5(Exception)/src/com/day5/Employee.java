package com.day5;


public class Employee 
{
	 private int id;
	 private String name;
	 private double Salary;
	
	
	 public Employee(int id, String name, double salary) 
	 {
		  super();
		  this.id = id;
		  this.name = name;
		  this.Salary = salary;
	 }
	
	
	 public Employee() {
		// TODO Auto-generated constructor stub
	}


	 public int getId() {
		return id;
	}


	 public String getName() {
		 return name;
	 }


	 public double getSalary() {
		 return Salary;
	 }


	 @Override
	 public String toString() 
	 {
		 return "Id: " + id + ", Name: " + name + ", Salary: " + Salary;
	 }
	 
}