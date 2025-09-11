import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.test.UserService;

class UserServiceTest {
	
	private final UserService us = new UserService();

//	@Test
//	void testConnection() {
//		actualValue = DBConnUtil.getConn();
//		assertNotNull(actualValue , " DB Connection failure");
//	}
//		
//	}
//	
	//Positive test case
	@Test
	void testValidUser() {
		assertTrue(us.isValidUser("kashif", "1234567")); // assuming that this user name exist 
	}
	
	// Negative test case
	@Test
	void testInValidUser()
	{
		
		assertFalse(us.isValidUser("Hello", "abc123")); // To check invalid user that does not exist in a table
	}
	
	@Test
	void testShortPassword()
	{
		
		assertTrue(us.isValidUser("kashif", "1234567")); // checking the password length which should be greater than 6
	}
	
	@Test
	void testEmptyInput()
	{
		
		assertFalse(us.isValidUser("", "")); // checking the empty fields 
	}

}
