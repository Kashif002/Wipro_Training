package com.wipro.service;

import org.springframework.stereotype.Service;

import com.wipro.dao.StudentDaoImpl;
import com.wipro .entity.Student;

@Service
public class StudentService {
	
	private final StudentDaoImpl dao;

	public StudentService(StudentDaoImpl dao) {
		super();
		this.dao = dao;
	}
	
	
	public void saveData(Student student)
	{
		dao.saveStudent(student);
	}
	
	public Student getStudent(Long id)
	{
		
		return dao.getStudent(id);
	}
	

}