/* Coyright Eric Cariou, 2009 - 2011 */

package service.id;

import communication.ProcessIdentifier;
import java.util.Vector;

/**
 * Data containing all identifiers of all the processes
 */
public class AllIdData extends IdentificationData {

    protected Vector<ProcessIdentifier> identifiers;

    public Vector<ProcessIdentifier> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(Vector<ProcessIdentifier> identifiers) {
        this.identifiers = identifiers;
    }

    public AllIdData(Vector<ProcessIdentifier> identifiers) {
        this.identifiers = identifiers;
    }
}