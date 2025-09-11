package com.wipro;

import java.util.Scanner;

public class Student
{
	
	int id;
	String name;
	int[] marks;
	

	//Constructor
	public Student(int id, String name, int[] marks) {
		super();
		this.id = id;
		this.name = name;
		this.marks = marks;
	}

	public void displayInfo()
	{
		System.out.println("Student Id :" + id);
		System.out.println("Student Name :" + name);
		System.out.println("Student Marks :" );
		for(int mark : marks)
		{
			System.out.print(mark + " ");
		}
		System.out.println("\n..");
	
		
	}


	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		
		Scanner sc = new Scanner(System.in);
		
		System.out.println("How many students you want to add in a Batch");
		int batchSize = sc.nextInt();
		Student[] students = new Student[batchSize];
		
		for(int i= 0 ; i<students.length ; i++)
		{
			System.out.println("Enter student Id: ");
			int id = sc.nextInt();
			
			System.out.println("Enter student name: ");
			String name = sc.next();
			
			System.out.println("Enter no of subjects :");
			int n = sc.nextInt();
			int[] marks = new int[n];
			
			System.out.println("Enter marks for " + n + "subjects");
			for(int j=0;j<marks.length;j++)
			{
				System.out.println("Subject " + (j+1) + " : ");
				marks[j] = sc.nextInt();
			}
			
			students[i] = new Student(id,name,marks);
		}
		
		System.out.println("Students details are given below : " );
		for(Student s : students)
		{
			s.displayInfo();
		}
		sc.close();
	}

}
