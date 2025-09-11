package com.test;

public class Credentials {
	
	String username;
	String password;
	
	public Credentials() 
	{
	    setUsername();
	    setPassword();
	}



	public void setUsername() {
	    username = "root";
	}

	public void setPassword() {
	    password = "My@SQL*%$23";
	}



	public String getUsername() {
		return username;
	}


	public String getPassword() {
		return password;
	}


	
}
