package Chatr.Conversation;

import org.junit.Test;
import static org.junit.Assert.*;
import Chatr.Converstation.User;

public class UserTest {

	
	@Test
	public void userNameUpdate(){
		User u = new User("Simon");
		assertEquals("Simon", u.getUserName());
		u.setUserName("Matthias");
		assertEquals("Matthias", u.getUserName());
	}
	
	@Test
	public void userID(){
		User u = new User("Simon");
		assertFalse(u.getUserID().matches("\\d+"));
	}
	
	
}
