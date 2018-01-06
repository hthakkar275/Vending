package exceptions;

public class InvalidMoneyDenominationsException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidMoneyDenominationsException(String message) {
        super(message);
    }
}
