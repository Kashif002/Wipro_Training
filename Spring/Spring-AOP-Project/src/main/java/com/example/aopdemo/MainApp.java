package com.example.aopdemo;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.example.aopdemo.service.StudentService;

import org.springframework.context.ApplicationContext;

public class MainApp {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        StudentService student = context.getBean(StudentService.class);

        student.enroll("Kashif");
        student.getStudent(101);

        try {
            student.throwError();
        } catch (Exception e) {
            System.out.println("Exception caught in main: " + e.getMessage());
        }

    }
}
