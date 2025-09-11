package com.bank;

// Bean , Pojo , Entity ( where we private fields , getter/setter, constructor , toString()), to share the 
//data between JSP pages
public class LoanBean {
	
	private String customerName;
	
	private double loanAmount;
	private int tenure;
	private double emi;
	
	
	//Values are injected through  getter and setter -- DI
	
	
	
	
	public String getCustomerName() {
		return customerName;
	}
	public LoanBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public double getLoanAmount() {
		return loanAmount;
	}
	public void setLoanAmount(double loanAmount) {
		this.loanAmount = loanAmount;
	}
	public int getTenure() {
		return tenure;
	}
	public void setTenure(int tenure) {
		this.tenure = tenure;
	}
	public double getEmi() {
		return emi;
	}
	public void setEmi(double emi) {
		this.emi = emi;
	}
	
	
	

}
