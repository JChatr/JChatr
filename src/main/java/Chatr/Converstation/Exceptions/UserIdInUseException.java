package Chatr.Converstation.Exceptions;

/**
 * Created by maximilianmerz on 16.05.17.
 */
public class UserIdInUseException extends RuntimeException {

	@Override
	public String toString() {
		return super.toString();
	}

	public UserIdInUseException(String errormessage){errormessage.toString();}
}
