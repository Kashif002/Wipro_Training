package com.example;

public class EmployeeProject {

	private int id;
	private String name;
	private double salary;
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name.trim();
	}
	
	public void setSalary(double salary) {
		if(salary > 50000) {
			System.out.println("Salary should be less than 50000");
			System.exit(0);
			
		}
		else {
			this.salary = salary;
		}
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public double getSalary() {
		return salary;
	}
	
	public static void main(String[] args) {
		EmployeeProject employee = new EmployeeProject();
		employee.setId(101);
		employee.setName("Sofiya");
		employee.setSalary(40000);
		System.out.println("employee Id :" + employee.getId() + "Employee NAme : "+ employee.getName() +" Employee salary : "+ employee.getSalary());	
		
	}	
}