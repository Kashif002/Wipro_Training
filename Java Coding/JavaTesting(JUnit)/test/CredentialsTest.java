import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.test.Credentials;
//create a simple test case to test the user name and the password 

class CredentialsTest {


		static Credentials c = null;
		static int i = 0;
		
		@BeforeAll
		static void setUpBeforeClass() throws Exception {
		 c = new Credentials();
		 System.out.println("Credentials Class Object is initialized");
		}

		@AfterAll
		static void tearDownAfterClass() throws Exception {
		c = null;
		 System.out.println("Credentials Class Object is null");
		}

		@BeforeEach
		void setUp() throws Exception {
		}

		@AfterEach
		void tearDown() throws Exception {
			
			i++;
			System.out.println("Test case : " + i + " is passed");
		}

		@Test
		void testUsername() {
			c.setUsername();
			assertEquals("root",c.getUsername()); 	
		}
		@Test
		void testPassword() {
			c.setPassword();
			assertEquals("My@SQL*%$23",c.getPassword()); 
			
		}
	}
