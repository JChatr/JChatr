package Chatr.Server;

import org.junit.Before;

import java.net.UnknownHostException;

import static org.junit.Assert.assertEquals;

public class LoginTest {

	/**
	 * guarantees the server is started for every test
	 */
	@Before
	public void start() {
		try {
			new Thread(new Server(3456)).start();
		} catch (UnknownHostException e) {
		}
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

}

