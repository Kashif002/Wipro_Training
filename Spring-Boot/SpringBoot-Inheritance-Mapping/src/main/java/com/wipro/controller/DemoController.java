package com.wipro.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wipro.dto.StudentDTO;
import com.wipro.dto.Student_DTO;
import com.wipro.entity.Student;
import com.wipro.entity.Teacher;
import com.wipro.entity.base.Address;
import com.wipro.entity.base.Person;
import com.wipro.repo.PersonRepo;
import com.wipro.repo.TeacherRepo;
import com.wipro.service.StudentService;



@RestController
@RequestMapping("/api")
public class DemoController {
	
	private final  StudentService studentService;
	private final TeacherRepo teacherRepo;
	private final PersonRepo personRepo;
	
	
	public DemoController(StudentService studentService,TeacherRepo teacherRepo,
			PersonRepo personRepo)
	{
		
		this.studentService = studentService;
		this.teacherRepo = teacherRepo;
		this.personRepo = personRepo;
		
	}
	
	@PostMapping("/add")
	public String add()
	{
		
		Teacher t1 = new Teacher("Niti" , new Address("JK" , "Delhi" , "110018") , "IT");
		teacherRepo.save(t1);
		
		
		Student s1 = new Student("Ravi" , new Address("GPG" , "Gopalganj" , "841440"));
		s1.addEmail("abc@gmail.com");
		s1.addEmail("xyz@Yahoo.co.in");
		
		
		Student s2 = new Student("Jk" , new Address("GKP" , "Gorakhpur" , "3545445"));
		s2.addEmail("jk@gmail.com");
		s2.addEmail("jk@Yahoo.co.in");
		
		
		studentService.save(s1);
		studentService.save(s2);
		
		
		
		return "Record Added";
		
	}
	
		@GetMapping("/studentsdata")
		public List<Student_DTO> students()
		{
			return studentService.findAllWithEmails().stream()
					.map(s-> new Student_DTO(s.getId(),
							s.getName(),
							s.getAddress().getCity(),
							s.getEmails().stream().map(e-> e.getEmail()).toList())).collect(Collectors.toList());
			
			
		
		}
		
		
		@GetMapping("/people")
		public void findAllPeople()
		{
			// Hibernate will provide the data with one query which Hibernate will generate polymorphic behaviour or a query (which will return both Student + Teacher) 
			List<Person> people =  personRepo.findAll();
			
			for(Person p :people)
				if(p instanceof Student s)
				System.out.println("Student "+  s.getName() + " Course" + s.getAddress());
				else if(p instanceof Teacher t)
				System.out.println("Teacher "+  t.getName() + " Subject" + t.getSubjects());
		
		}
		
		
//		@GetMapping("/students")
//		public void findAllStudents()
//		{
//			 personRepo.findAllStudents().forEach(s->System.out.println("Students Names "+ s.getName()));
//			 
//			
//		}
		
		
		@GetMapping("/teachers")
		public void findAllTeachers()
		{
			personRepo.findAllTeachers().forEach(t->System.out.println("Teacher Names "+ t.getName()));;
			
		}
		
//		@GetMapping("/students_dto")
//		public List<Student_DTO> student_dto()
//		{
//			return StudentService().stream()
//					.map(s-> new StudentDTO(s.getId(),
//							s.getName(),
//							s.getAddress().getCity()
//							.collect(Collectors.toList())));
//			
//			
//		
//		}
	

}