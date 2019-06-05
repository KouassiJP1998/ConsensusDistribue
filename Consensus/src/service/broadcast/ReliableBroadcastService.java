/* Coyright Eric Cariou, 2009 - 2011 */

package service.broadcast;

import communication.CommunicationException;
import communication.Message;
import service.IBroadcast;
import service.IIdentification;
import service.Service;

public class ReliableBroadcastService  extends Service implements IBroadcast {

    protected IIdentification idService;

    public void setIdentificationService(IIdentification idService) {
        this.idService = idService;
    }

    public void broadcast(Object data) throws CommunicationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Message synchDeliver() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Message asynchDeliver() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean availableMessage() {
        throw new UnsupportedOperationException("Not supported yet.");
    }



}
