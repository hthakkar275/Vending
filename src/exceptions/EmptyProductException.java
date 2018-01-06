package exceptions;

public class EmptyProductException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1001L;

	public EmptyProductException(String message) {
		super(message);
	}
}
