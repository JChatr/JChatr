package Chatr.Chat;

import org.junit.Test;

import static org.junit.Assert.*;

import Chatr.Model.User;

public class UserTest {


	@Test
	public void userNameUpdate() {
		User u = new User("@sHaag")
				.setUserName("Simon");
		assertEquals("Simon", u.getUserName());
		u.setUserName("Matthias");
		assertEquals("Matthias", u.getUserName());
	}


}
