package Chatr;

import org.junit.Test;

import Chatr.Client.Connection;
import Chatr.Converstation.User;
import Chatr.Server.Server;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;

public class LoginTest {
	
	@Before
	public void start(){
		new Thread(new Server()).start();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void createUser(){
		String userID = "" + System.nanoTime();
		User userData = new User(userID);
		Connection.createUser(userID, userData);
		assertEquals(userData, Login.loginUser(userID));
		Connection.deleteUser(userID);
	}
	
	@Test
	public void readUser(){
		String userName = "bob";
		User user = new User(userName);
		Connection.createUser(userName, user);
		System.out.println(Connection.readUser(userName));
		assertEquals(Connection.readUser(userName), Login.loginUser(userName));
	}
	
}

