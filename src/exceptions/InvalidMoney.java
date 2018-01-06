package exceptions;

public class InvalidMoney extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1001L;

	public InvalidMoney(String message) {
		super(message);
	}
}
