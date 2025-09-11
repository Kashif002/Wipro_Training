package com.wipro.entity;

import java.util.ArrayList;
import java.util.List;

import com.wipro.entity.base.Address;
import com.wipro.entity.base.Person;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

@Entity
@DiscriminatorValue("TEACHER")
public class Teacher extends Person {

	private String subject;
	
	
	public Teacher()
	{}
	public Teacher(String name , Address addr , String subject)
	{
		
		super(name , addr);
		this.subject = subject;
	}
	
	@OneToMany(mappedBy="student" , cascade = CascadeType.ALL , orphanRemoval=true, fetch=FetchType.LAZY)
	private List<Email> emails = new ArrayList<>();


	public List<String> getSubjects() {
	    return getSubjects();
	}

	public List<Email> getEmails() {
	    return emails;
	}
	
}