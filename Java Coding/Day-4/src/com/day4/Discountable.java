package com.day4;


//In interface generally we are not writing any logic part or implementation so that's why 
//it's an public so that we can access methods 
public interface Discountable {
	
	 double calculateDiscount();  // by default it's an abstract method

}


------------------------------------------------------------

package com.example.oops;

public class SeasonalProduct implements Discountable{
	
	private String name;
	private double price;
	
	public SeasonalProduct(String name, double price) {
		super();
		this.name = name;
		this.price = price;
	}
	
	@Override
	public double calculateDiscount() {
		// TODO Auto-generated method stub
		return price*0.10;
	}

	
	public void show()
	{
		
	//	System.out.println("Seasonal Discount : "+name+price+calculateDiscount());
		
 System.out.printf("Seasonal : %s | Price: Rs. %.2f | Discount: Rs. %.2f%n" ,name,price,calculateDiscount());
	}




	
	
	

}

---------------------------------------------------------------------


package com.example.oops;

public class ClearanceProduct implements Discountable{
	
	private String name;
	private double price;
	
	public ClearanceProduct(String name, double price) {
		super();
		this.name = name;
		this.price = price;
	}
	@Override
	public double calculateDiscount() {
		// TODO Auto-generated method stub
		return price*0.50;
	}
	
	public void show()
	{
		
		System.out.printf("Clearance : %s | Price: Rs. %.2f | Discount: Rs. %.2f%n" ,name,price,calculateDiscount());  
	}
	
	
}


------------------------------------------------------------------------------------


package com.example.oops;

//Main Program of Interface creating an array of Discountable Interface and storing the object of 
//two classes (SeasonalProduct.java and ClearanceProduct.java) implementing the abstract method of an Interface
public class InterfaceDiscountAppExample {

	public static void main(String[] args) {
		
		// As we know that we cannot create an object of Interface ,
		//so here we are creating an array for allocating only a space of n for holding the references to an objects of those classes who all are implementing the Discountable interface 
		
		Discountable[] discounts = new Discountable[2];
		
		// for eg: String[] s = new String[2];
		
		      
		
		discounts[0] = new SeasonalProduct("Laptop" , 3000);
		discounts[1] = new ClearanceProduct("Old SmartPhone" , 5000);
		
		System.out.println("Discount Summary Report");
		
		for(Discountable d : discounts)
		{
			
			if(d instanceof SeasonalProduct sp) sp.show();
			else if(d instanceof ClearanceProduct cp) cp.show();
			
		}

	}

}
