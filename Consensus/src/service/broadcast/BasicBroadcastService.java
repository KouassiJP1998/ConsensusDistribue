/* Coyright Eric Cariou, 2009 - 2011 */

package service.broadcast;

import communication.CommunicationException;
import communication.CompoundException;
import communication.Message;
import communication.ProcessIdentifier;
import java.util.Iterator;
import service.IIdentification;
import service.MessageType;
import service.Service;
import service.TypedMessage;
import service.IBroadcast;

/**
 * Implementation of a basic broadcast algorithm
 */
public class BasicBroadcastService extends Service implements IBroadcast {

    protected IIdentification idService;

    public void setIdentificationService(IIdentification idService) {
        this.idService = idService;
    }

    public void broadcast(Object data) throws CommunicationException {
        ProcessIdentifier id;
        Iterator it;
        CompoundException exceptions = null;
        CommunicationException firstException = null;
        it = idService.getAllIdentifiers().iterator();
        // send the data to all the processes
        while (it.hasNext()) {
            id = (ProcessIdentifier) it.next();
            try {
                // simulate the crash of the process during the broadcast
                commElt.crashProcess();
                commElt.sendMessage(new TypedMessage(id, data, MessageType.BASIC_BROADCAST));
            } catch (CommunicationException e) {
                if (firstException == null) firstException = e;
                else {
                    if (exceptions == null) {
                        exceptions = new CompoundException();
                        exceptions.addException(firstException);
                    }
                    exceptions.addException(e);
                }
            }
        }
        if (exceptions != null) throw exceptions;
        if (firstException != null) throw firstException;
     }

    public Message synchDeliver() {
        return buffer.removeElement(true);
    }

    public Message asynchDeliver() {
        return buffer.removeElement(false);
    }

    public boolean availableMessage() {
        return buffer.available() > 0;
    }
}
