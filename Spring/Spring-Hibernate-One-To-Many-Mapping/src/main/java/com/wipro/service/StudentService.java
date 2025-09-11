package com.wipro.service;

import org.springframework.stereotype.Service;

import com.wipro.dao.IStudentDao;
import com.wipro .entity.Student;

@Service
public class StudentService {
	
	private final IStudentDao dao;

	public StudentService(IStudentDao dao) {
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