package Chatr.Model;

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

	public boolean isErrorexisting(){ return errorexisting; }

	public ErrorMessagesValidation(boolean errorexisting, String[] errorMessages){
		this.errorexisting = errorexisting;
		this.errorMessages = errorMessages;
	}

}
