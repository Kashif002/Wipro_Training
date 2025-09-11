package com.example.aopdemo.service;

import org.springframework.stereotype.Component;

@Component
public class StudentService {

    public String enroll(String name) {
        System.out.println("Enrolling student: " + name);
        return "Enrolled: " + name;
    }

    public String getStudent(int id) {
        System.out.println("Fetching student with ID: " + id);
        return "Student-" + id;
    }

    public void throwError() {
        throw new RuntimeException("Student not found!");
    }
}
