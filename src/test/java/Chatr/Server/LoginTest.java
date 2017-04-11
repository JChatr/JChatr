package Chatr.Server;

import Chatr.Helper.Terminal;
import Chatr.Login;
import org.junit.Assert;
import org.junit.Test;

import Chatr.Client.Connection;
import Chatr.Converstation.User;
import Chatr.Server.Server;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;

import java.io.*;

public class LoginTest {

	private final ByteArrayOutputStream syso = new ByteArrayOutputStream();

	@Before
	public void start(){
		new Thread(new Server()).start();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test(expected = IllegalStateException.class)
	public void userWithOutAt(){
		ByteArrayInputStream in = new ByteArrayInputStream(("abc\nabc\nabc\nabc\n").getBytes());
		System.setIn(in);
		String userID = "abc";
		Login.loginUser(userID);
	}

	@Test
	public void userWithLaterAt(){
		ByteArrayInputStream in = new ByteArrayInputStream(("abc\nabc\nabc\n@abc\n\n\n").getBytes());
		System.setIn(in);
		User userData = new User("@abc");
		Connection.createUser("@abc", userData);
		Assert.assertEquals(userData, Login.loginUser("abc"));
		Connection.deleteUser("@abc");
	}


	@Test
	public void createUser(){
		String userID = "@" + System.nanoTime();
		User userData = new User(userID);
		Connection.createUser(userID, userData);
		Assert.assertEquals(userData, Login.loginUser(userID));
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

