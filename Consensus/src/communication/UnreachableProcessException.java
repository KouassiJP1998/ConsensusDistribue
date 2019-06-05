/* Coyright Eric Cariou, 2009 - 2011 */

package communication;

/**
 * Exception throw if a remote process is not reachable. In TCP context, it generally means
 * that it is not possible to connect to the remote server socket used by the remote process.
 */
public class UnreachableProcessException extends CommunicationException {

	/**
	 * 
	 */
	public UnreachableProcessException() {
		
	}

	/**
	 * @param message
	 */
	public UnreachableProcessException(String message) {
		super(message);
		
	}

	/**
	 * @param cause
	 */
	public UnreachableProcessException(Throwable cause) {
		super(cause);
		
	}

	/**
	 * @param message
	 * @param cause
	 */
	public UnreachableProcessException(String message, Throwable cause) {
		super(message, cause);
		
	}

}
