package Chatr.Server;

import Chatr.Client.Connection;
import Chatr.Model.User;
import Chatr.Helper.Terminal;
import Chatr.Controller.Login;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class LoginTest {

	/**
	 * guarantees the server is started for every test
	 */
	@Ignore
	public void start() {
		//new Thread(new Server()).start();
	}

    /*
	@Test(expected = IllegalStateException.class)
	public void userWithOutAt() {
		ByteArrayInputStream in = new ByteArrayInputStream(("abc\nabc\nabc\nabc\nabc\n").getBytes());
		setTerminalInput(in);
		System.out.println("sadf ");
		String userID = "abc";
		Login.registerUser(userID);
	}

	@Test
	public void userWithLaterAt() {
		ByteArrayInputStream in = new ByteArrayInputStream(("abc\nabc\nabc\n@abc\n\n\n").getBytes());
		setTerminalInput(in);
		User userData = new User("@abc");
		Connection.createUser("@abc", userData);
		Assert.assertEquals(userData, Login.registerUser("abc"));
	}
    */

    /*
	@Test
	public void createUser() {
		String userID = "@dTrump";
		User userData = new User(userID);
		Connection.createUser(userID, userData);
		Assert.assertEquals(userData, Login.registerUser(userID));
	}

	@Test
	public void readUserExistent() {
		String userID = "@aMerkel";
		User user = new User(userID);
		Connection.createUser(userID, user);
		assertEquals(Connection.readUser(userID), Login.registerUser(userID));
	}

	@Test
	public void readUserNonExistenet() {
		User u1 = new User("@random");
		u1.setUserName("Donald Trump");
		u1.setEmail("makeAmerica@great.gov");

		ByteArrayInputStream in = new ByteArrayInputStream("Donald Trump\nmakeAmerica@great.gov\n".getBytes());
		setTerminalInput(in);

		assertEquals(u1, Login.registerUser("@random"));
	}
	*/

	/**
	 * change the input stream after the Terminal scanner has been created
	 * @param is Input Stream to replace STDIN with
	 */
	private void setTerminalInput(InputStream is) {
		try {
			Field scanner = Terminal.class.getDeclaredField("scan");
			scanner.setAccessible(true);
			scanner.set(null, new Scanner(is));
		} catch (NoSuchFieldException | IllegalAccessException e) {
		}
	}
}

