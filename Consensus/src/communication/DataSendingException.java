/* Coyright Eric Cariou, 2009 - 2011 */

package communication;

/**
 * Exception thrown in case of communication problem during a message transmission
 *
 */
public class DataSendingException extends CommunicationException {

	/**
	 * 
	 */
	public DataSendingException() {
		
	}

	/**
	 * @param message
	 */
	public DataSendingException(String message) {
		super(message);
		
	}

	/**
	 * @param cause
	 */
	public DataSendingException(Throwable cause) {
		super(cause);
		
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DataSendingException(String message, Throwable cause) {
		super(message, cause);
		
	}

}
