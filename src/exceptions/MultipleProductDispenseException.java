package exceptions;

public class MultipleProductDispenseException extends Exception
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MultipleProductDispenseException(String message)
    {
        super(message);
    }
}
