package com.wipro.main;

import com.wipro.config.HibernateConfig;
import com.wipro.doa.StudentDao;
import com.wipro.model.Student;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class MainApp {
    public static void main(String[] args) {
        // Initialize Spring context
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(HibernateConfig.class);
        StudentDao studentDao = context.getBean(StudentDao.class);

        // Insert a new student with ID 101 and name "Kashif"
        Student student1 = new Student();
         // Manually set ID for demonstration
        student1.setId(101L);
        student1.setName("Kashif");
        studentDao.save(student1);
        System.out.println("Student inserted: " + student1);

        // Update the student's name to "Kashif"
        student1.setName("Kasihf Anjum");
        studentDao.update(student1);
        System.out.println("Student updated: " + student1);

        // Retrieve the student with ID 101
        Student retrievedStudent = studentDao.findById(101L);
        System.out.println("Retrieved Student: " + retrievedStudent);

        // Insert additional students for demonstration
        Student student2 = new Student();
         // Manually set ID for demonstration
        student2.setId(102L);
        student2.setName("Sana");
        studentDao.save(student2);

        Student student3 = new Student();
        // Manually set ID for demonstration
        student3.setId(103L); 
        student3.setName("Samar");
        studentDao.save(student3);

        // Retrieve all students before deletion
        System.out.println("All Students Before Deletion:");
        List<Student> studentsBeforeDeletion = studentDao.findAll();
        studentsBeforeDeletion.forEach(System.out::println);

        // Delete the student with ID 102
        studentDao.delete(102L);
        System.out.println("Student with ID 102 deleted.");

        // Retrieve all students after deletion
        System.out.println("All Students After Deletion:");
        List<Student> studentsAfterDeletion = studentDao.findAll();
        studentsAfterDeletion.forEach(System.out::println);

        // Close the Spring context
        context.close();
    }
}