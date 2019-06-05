/* Coyright Eric Cariou, 2009 - 2011 */

package service.id;

import communication.ProcessIdentifier;

/**
 * Request for a process to be removed from the identifier list. I.e., it leaves the system.
 */
public class RemoveIdData extends ProcessIdData {

    /**
     * @param processId the identifier of the process leaving the system
     */
    public RemoveIdData(ProcessIdentifier processId) {
       super(processId);
    }

}
