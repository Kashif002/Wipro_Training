package com.wipro;

public class StringExample {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// Creating a string object using a literal
		
		String s = "Niti";  // String Pool in Heap 
		System.out.println(s.hashCode());
		// this gives the actual memory identity where it is stored
		System.out.println(System.identityHashCode(s));
		//  creating a string object using new keyword
		
		String s1 = new String("Nitin"); // Heap  
		
		System.out.println(s1.hashCode());  //if same content as S object has then it will give the same hash
		System.out.println(System.identityHashCode(s1)); // different memory and different value
		
		
		// Whenever the String class overrides
		// the hashCode() method then it is computing on the basis 
		// of the content not the memory location
		
		System.out.println(s==s1); // different memory references
		System.out.println(s.equals(s1)); // it check the content
		
	
		
		//2nd use case with String concatenation how it works
		
		String svalue1 = "Niti";
		String svalue2 = "Ni" + "ti";   // compiler will treat it as String svalue2 = "Niti" at compile time 
		
		System.out.println(svalue1 == svalue2);  // true  // It is compile time 
		
		String svalue3 = "Ni"; 
		String svalue4 ="ti";
		String oldvalue = "Niti";
		String newvalue = svalue3+svalue4; // It will be evaluated at run time // it will be a new object on heap 
		System.out.println(newvalue);
		System.out.println(oldvalue == newvalue); // false
		System.out.println(oldvalue.equals(newvalue)); // true
		
		
		
		final String svalue5 = "Ni"; 
		final String svalue6 ="ti";
		String oldvalue1 = "Niti";
		String newvalue1 = svalue5+svalue6; //it will be constant at compile time only  -- So compiler will recognize the final values as constant at compile time
		System.out.println(newvalue1);
		System.out.println(oldvalue1 == newvalue1); // true
		System.out.println(oldvalue1.equals(newvalue1)); // true
		
		
		String a = "Niti";
		String b = new String("Niti");
		
		String c = b; 
		
		System.out.println(a==b); //false 
		System.out.println(a==c); // false
		
		
		String a1 = "Niti";
		String b1 = new String("Niti");
		
		String c1 = b1.intern(); // we are manually telling to the compiler to put this string into the pool 
		
		System.out.println(a1==b1); //false 
		System.out.println(a1==c1); // true
		
	}

}

// literal 
// new keyword
// hashCode()
// identityHashCode()
// concatenation at compile time and runtime
// ==  ( comparing the memory reference)
// .equals (comparing the content)
// .intern() (forcing manually to use it as a string pool version)

