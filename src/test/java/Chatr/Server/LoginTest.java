package Chatr.Server;

import Chatr.Controller.Login;
import Chatr.Model.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LoginTest {

	/**
	 * guarantees the server is started for every test
	 */
	@Before
	public void start() {
		new Thread(new Server()).start();
	}



	@Test
	public void registerValidUser() {
		User l = Login.registerUser("@randomBullshit", "test@testmail.com", "Tester", "supersecret");
		User ref = new User("@randomBullshit")
				.setUserName("Tester")
				.setEmail("test@testmail.com")
				.setPassword("supersecret");
		assertEquals(ref, l);
	}

	@Test
	public void loginValidUser() {
		User l = Login.loginUser("@aMerkel", "42IsNotTheSolution");
		User ref = new User("@aMerkel")
				.setUserName("Angela Merkel")
				.setEmail("angela@merkel.de")
				.setPassword("42IsNotTheSolution");
		assertEquals(ref, l);
	}
}

