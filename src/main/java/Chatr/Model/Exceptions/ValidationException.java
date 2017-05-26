package Chatr.Model.Exceptions;

public class ValidationException extends RuntimeException {

	private final String errorMessage;

	public ValidationException(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	@Override
	public String toString() {
		return getErrorMessage();
	}
}
