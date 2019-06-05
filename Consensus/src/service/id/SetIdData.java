/* Coyright Eric Cariou, 2009 - 2011 */

package service.id;

import communication.ProcessIdentifier;

/**
 * Response to a process after an identifier request: the process received its identification number.
 */
public class SetIdData extends ProcessIdData {

    public SetIdData(ProcessIdentifier processId) {
        super(processId);
    }
}
