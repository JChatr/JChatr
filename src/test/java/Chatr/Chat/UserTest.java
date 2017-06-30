package Chatr.Chat;

import org.junit.Test;

import static org.junit.Assert.*;

import Chatr.Model.User;

public class UserTest {

// TODO WRITE SOME TEST HERE !!!!
	@Test
	public void userNameUpdate() {
		User u = new User("Simon", "@sHaag", "sh123@hdm-stuttgart.de", "12345");
		assertEquals("@sHaag", u.getID());
		assertEquals("Simon", u.getUserName());
		assertEquals("sh123@hdm-stuttgart.de", u.getEmail());
	}


}
