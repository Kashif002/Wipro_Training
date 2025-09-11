package com.wipro.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.wipro.dto.Student_DTO;
import com.wipro.dto.Teacher_DTO;
import com.wipro.entity.Student;
import com.wipro.repo.StudentRepo;
import com.wipro.repo.TeacherRepo;


@Service
public class StudentService {
	

	private final StudentRepo students;
    private final TeacherRepo teacher;
    
	public StudentService(StudentRepo students,TeacherRepo teacher) {
		super();
		this.students = students;
		this.teacher=teacher;
	}
	public Student save(Student s ) {
		
		return students.save(s);
	}
	
	public List<Student> findAllWithEmails()
	{
		
		return students.findAll();
	}
	
	
		
		public List<Student_DTO> allStudents_DTO()
	{
			return (List<Student_DTO>) students.findAllStudents().stream()
					.map(s-> new Student_DTO(s.getId(),
							s.getName(),
							s.getAddress().getCity(), null))
						.collect(Collectors.toList());
	}
		
		public List<Teacher_DTO> allTeacher_DTO()
	{
			return (List<Teacher_DTO>) teacher.findAllTeachers().stream()
					.map(s-> new Teacher_DTO(s.getId(),
							s.getName(),
							s.getAddress().getCity()))
						.collect(Collectors.toList());
	}


}