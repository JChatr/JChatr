package Chatr.Chat;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.junit.Test;

import static org.junit.Assert.*;

import Chatr.Model.User;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UserTest {


	@Test
	public void userNameUpdate() {
		User u = new User("Simon");
		assertEquals("Simon", u.getUserName());
		u.setUserName("Matthias");
		assertEquals("Matthias", u.getUserName());
	}

	@Test
	public void userID() {
		User u = new User("Simon");
		assertFalse(u.getUserID().matches("\\d+"));
	}


}
