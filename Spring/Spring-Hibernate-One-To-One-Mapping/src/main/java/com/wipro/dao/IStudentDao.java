package com.wipro.dao;

import com.wipro.entity.Student;

public interface IStudentDao {
	
	public void saveStudent(Student student);
	public Student getStudent(Long id) ;
}