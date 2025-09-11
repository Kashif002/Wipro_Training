package com.wipro;

import java.util.Arrays;

//Creating Employee Class
class Students{
	int id;
	String name;
	int[] marks;
	
	Students(int id,String name,int[] marks)
	{
		this.id=id;
		this.name=name;
		this.marks=marks;
	}
	
	void display()
	{
		System.out.println("Id = "+ id +"\nName = " + name + "\nMarks = " + Arrays.toString(marks));
	}
}

public class Array {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Students s1= new Students(1,"Kashif",new int[] {90,90,85});
		s1.display();
		
		
	}

}
