package Chatr.Model;

import java.util.Arrays;
import java.util.Objects;

/**
 * Created by maximilianmerz on 03.06.17.
 */
public class ErrorMessagesValidation {
	private String[] errorMessages = new String[4];
	private boolean errorexisting = false;

	public String[] getErrormessages() {
		return errorMessages;
	}

	public void setErrormessages(String[] errorMessages) {
		this.errorMessages = errorMessages;
	}

	public void setErrorexisting(boolean errorexisting) {
		this.errorexisting = errorexisting;
	}

	public boolean isErrorexisting() {
		return errorexisting;
	}

	public String getUserIdErrorMessage() {
		return errorMessages[0];
	}

	public void setUserIdErrorMessage(String userIdErrorMessage) {
		this.errorMessages[0] = userIdErrorMessage;
	}

	public String getEmailErrorMessage() {
		return errorMessages[1];
	}

	public void setEmailErrorMessage(String emailErrorMessage) {
		this.errorMessages[1] = emailErrorMessage;
	}

	public String getPasswordErrorMessage() {
		return errorMessages[2];
	}

	public void setPasswordErrorMessage(String passwordErrorMessage) {
		this.errorMessages[2] = passwordErrorMessage;
	}

	public String getUsernameErrorMessage() {
		return errorMessages[3];
	}

	public void setUsernameErrorMessages(String usernameErrorMessages) {
		this.errorMessages[3] = usernameErrorMessages;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ErrorMessagesValidation) {
			ErrorMessagesValidation o = (ErrorMessagesValidation) obj;
			return Arrays.equals(o.errorMessages, this.errorMessages) &&
					Objects.equals(o.errorexisting, this.errorexisting);
		}
		return false;
	}
}
