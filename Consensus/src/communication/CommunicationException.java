/* Coyright Eric Cariou, 2009 - 2011 */

package communication;

/**
 * General exception in the context of the communication package
 */
public class CommunicationException extends Exception {

	/**
	 * 
	 */
	public CommunicationException() {
	
	}

	/**
	 * @param message
	 */
	public CommunicationException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public CommunicationException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public CommunicationException(String message, Throwable cause) {
		super(message, cause);
	}

}
