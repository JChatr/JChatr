package Chatr.Controller;

import Chatr.Client.Connection;
import Chatr.Converstation.Exceptions.ClientSyntaxException;
import Chatr.Converstation.Exceptions.UserIdInUseException;
import Chatr.Converstation.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class to login into your user account or to create one.
 *
 * @author mk285
 */
public class Login {

	public static boolean successful = false;
	private static String email;
	private static Logger log = LogManager.getLogger(Login.class);



	//TODO implement Logging

	public static User registerUser(String userIdInput, String eMailInput, String usernameInput, String passwordInput) {

		String userIdConfirmed = "";
		try{
			System.out.println(userIdInput + eMailInput + usernameInput + passwordInput);
			userIdAvaiableCheck(userIdInput);
			userIdConfirmed = syntaxUserId(userIdInput);
			syntaxEmail(eMailInput);
		}catch(ClientSyntaxException e){
			e.printStackTrace();
		}catch(UserIdInUseException e){
			e.printStackTrace();
		}

		User user = null;

		user = new User(userIdConfirmed);
		user.setUserName(usernameInput);
		user.setEmail(eMailInput);
		user.setPassword(passwordInput);

		Connection.createUser(userIdInput, user);
		log.info(String.format("created User %s|%s", user.getUserID(), user.getUserName()));
		return user;
	}



	//TODO Logging

	public static String syntaxUserId (String userIdUserInput)throws ClientSyntaxException{

		String errorMessage = "Invalid User-ID input.";
		String userIdWithAt = "@" + firstCharToLowerCase(userIdUserInput);
		if (userIdWithAt.matches("^@[a-z][a-zA-Z0-9]{4,28}$")) {
			log.info(String.format("UserId %s is permitted.", userIdWithAt));
			return userIdWithAt;
		} else {
			log.info(String.format("UserID %s is not permitted.", userIdWithAt));
			throw new ClientSyntaxException(errorMessage);
		}
	}

	//TODO Email Regex

	public static void syntaxEmail(String email) throws ClientSyntaxException {
		if(email.contains(".") && email.contains("@")){
			log.info(String.format("Email %s is permitted.", email));
		}else{
			log.info(String.format("Email %s is not permitted.", email));
			throw new ClientSyntaxException("Your email isn't legal.");
		}
	}

	public static void userIdAvaiableCheck(String userIdUserInput) throws UserIdInUseException{
		User user = null;
		if ((user = Connection.readUser(userIdUserInput)) != null) {
			log.info(String.format("read User %s|%s from server", user.getUserID(), user.getUserName()));
			log.info(String.format("UserId %s is already in use.", userIdUserInput));
			throw new UserIdInUseException("This User-ID is already in use.");
		}
		else{
			log.info(String.format("UserId %s is available.", userIdUserInput));
		}
	}

	
	public static String firstCharToLowerCase(String input) {
		if (input == null || input.length() == 0) {
			return input;
		}
		char c[] = input.toCharArray();
		c[0] = Character.toLowerCase(c[0]);
		return new String(c);
	}
}




