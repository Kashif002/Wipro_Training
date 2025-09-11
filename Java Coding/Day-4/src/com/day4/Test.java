package com.day4;

class Outer {

	 int x = 10;

	 

	 class Inner {

	 int y = 5;

	 

	 void display() {

	 System.out.println(x + y);

	 }

	 }

	}

	 

	public class Test {

	 public static void main(String[] args) {

	 Outer outer = new Outer();

	 Outer.Inner inner = outer.new Inner();

	 inner.display();

	 }
}

//class Test
//{
//int a;
//Test()
//{
//System.out.println("Default Consturctor");
//}
//}
//class Main
//{
//public static void main(String args[])
//{
//Test t1 = new Test(10);
//}
//}