package com.wipro.HIBORM.model;

import jakarta.persistence.*;

@Entity
public class Employee {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 
//    @Column(name="EmpName")
    private String name;
//    @Column( name= "EmpEmail", unique=true)
    @Column(unique=true)
    private String email;
    
    //No args constructor
	public Employee() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	//Args based constructor
	public Employee(Long id, String name, String email) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
	}
	
	//Args based constructor
		public Employee(String name, String email) {
			super();
			this.name = name;
			this.email = email;
		}
	
	
	//getter and setters
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	//To string for displaying value
	@Override
	public String toString() {
		return "Id: " + id + "		Name: " + name + "		Email: " + email;
	}
    
}