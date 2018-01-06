package exceptions;

public class InsufficientDepositException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1001L;

	public InsufficientDepositException(String message) {
		super(message);
	}
}
