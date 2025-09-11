package com.wipro;

public class VariablesInJava
 {
	 static int staticCount = 0; // Method Area
	 final double pi =3.14 ; // Heap (final instance variable)
	 String name ; //Heap
	 
	 
	 public void Input1()
	 {
		 
		 int localVar = 300; // stack
		 final int localConst = 200; // stack
		 
		 System.out.println(localVar + " " + localConst);
		 // you can access without an object reference and if you are calling in same class
		// System.out.println(count);
		 System.out.println(staticCount);
	 }
	 
	 public static void main(String[] ar)
	 {
		 // Object is in Heap & reference variables is in stack
		 VariablesInJava vij = new VariablesInJava();
		 System.out.println(vij.pi);
		 vij.Input1();
		 System.out.println(vij.staticCount);
		 System.out.println(VariablesInJava.staticCount);
		 System.out.println(staticCount);
		 
	 }
	 
 }
	

