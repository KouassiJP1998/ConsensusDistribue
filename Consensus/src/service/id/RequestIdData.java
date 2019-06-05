/* Coyright Eric Cariou, 2009 - 2011 */

package service.id;

import communication.ProcessIdentifier;

/**
 * Request for a process to get its identification number.
 */
public class RequestIdData extends ProcessIdData {

    /**
     * @param processId the identifier of the process. It contains the IP and port values, allowing
     * the identification server to know these informations.
     */
    public RequestIdData(ProcessIdentifier processId) {
        super(processId);
    }
}
