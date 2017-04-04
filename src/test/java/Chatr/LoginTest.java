package Chatr;

import org.junit.Test;

import Chatr.Client.Connection;
import Chatr.Converstation.User;
import static org.junit.Assert.*;

public class LoginTest {
	
	@Test
	public void createUser(){
		String userID = "" + System.nanoTime();
		User user = new User(userID);
		assertEquals(user, Login.loginUser(userID));
		Connection.deleteUser(userID);
	}
	
	@Test
	public void readUser(){
		String userID = "" + System.nanoTime();
		User user = new User(userID);
		Connection.createUser(userID, user);
		assertEquals(Connection.readUser(userID), Login.loginUser(userID));
	}
	
}
