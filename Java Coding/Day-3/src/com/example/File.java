package com.example;

public class BankAccount {
	
	private String accountHolder;
	private double balance;
	
	public BankAccount(String accountHolder, double balance) {
		super();
		this.accountHolder = accountHolder;
		this.balance = balance;
	}

	public class Transaction{
		
		public void deposit(double amount)
		{
			balance += amount;
			System.out.println(amount + "Rs. is Deposited and now the balance is :" + balance );
			
		}
		
		public void withdraw(double amount)
		{
			if(amount<= balance)
			{
			balance -= amount;
			System.out.println( "Withdrawl amount is " + amount + "and now the balance is :" + balance );
			}
		}
	}

 public void showBalance()
 {
	 
	 System.out.println("Account Holder :" + accountHolder + " Balance in your account is :" + balance);
 }


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		BankAccount acc = new BankAccount("Niti" , 5000);
		BankAccount.Transaction t = acc.new Transaction(); // the inner class object must be tied with outer class object
		System.out.println("The current Balance in your account is :");
		acc.showBalance();
		t.deposit(2000);
		acc.showBalance();
		t.withdraw(1000);
		acc.showBalance();
	}

}



package com.example;



// An utility builder class for an immutable  product

final class Product {
	
	private final String name;
	private final double price;
	
	
   // getting an object on ProductBuilder passed through Product class constructor using Build()
	private Product(ProductBuilder pB) {
		
		this.name = pB.name;
		this.price = pB.price;
	}

	public static class ProductBuilder
	{
		
		private String name; // cannot access it directly  , we will give setter method to them 
		private double price;
		
		public ProductBuilder setName(String name) {
			this.name = name;
			return this;
		}
		public ProductBuilder setPrice(double price) {
			
			this.price = price;
			return this;
		}
		
		
		public Product build()
		{
			
			return new Product(this); // return ProductBuilder class object
		}
		
}
	public void show() 
	{
		System.out.println("Product :" + name + " Price :" + price);
	}

	
}


package com.example;
import com.example.Product;


public class ProductMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//Through the ProductBuilder Class we are making Product class as Mutable
		Product p = new Product.ProductBuilder()
				.setName("Mouse")
				.setPrice(450.00)
				.build();
		p.show();		

	}	

}
