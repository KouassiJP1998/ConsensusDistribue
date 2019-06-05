/* Coyright Eric Cariou, 2009 - 2011 */

package communication;

import java.util.Vector;

/**
 * When more than one exception has to be handled, this exception stores several other ones.
 */
public class CompoundException extends CommunicationException {

    protected Vector<CommunicationException> exceptions = new Vector<CommunicationException>();

    public void addException(CommunicationException e) {
        exceptions.add(e);
    }

    public Vector<CommunicationException> getExceptions() {
        return exceptions;
    }
    
    public CompoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CompoundException(String message) {
        super(message);
    }

    public CompoundException() {
    }



}
