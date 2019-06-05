/* Coyright Eric Cariou, 2009 - 2011 */

package service;

import communication.CommunicationException;
import communication.Message;
import communication.ProcessIdentifier;

/**
 * Basic communication service for point to point communication between processes.
 *
 * The communication element embedded in an instance of the <code>DistributedServicesMiddleware</code>
 * class is already implementing this basic communication service. However, it is not possible to directly
 * call its operations as this will interfere with management of messages for others services 
 * (all messages for all services are received by this communication element). The goal of the
 * <code>ProxyCommunication</code> class is then to offer the same operations by being a proxy
 * avoiding interferences with other services.
 */
public class ProxyCommunication extends Service implements ICommunication {

    @Override
    public void sendMessage(Message msg) throws CommunicationException {
        commElt.sendMessage(new TypedMessage(msg.getProcessId(), msg.getData(), MessageType.NONE));
    }

    public void sendMessage(ProcessIdentifier id, Object data) throws CommunicationException {
        commElt.sendMessage(new TypedMessage(id, data, MessageType.NONE));
    }

    public Message synchReceiveMessage() {
        Message msg =buffer.removeElement(true);
        return msg;
    }

    public Message asynchReceiveMessage() {
        Message msg = buffer.removeElement(false);
        if (msg == null) return null;
        return msg;
    }

    public boolean availableMessage() {
        return (buffer.available() > 0);
    }

    public ProxyCommunication() {
    }

    public void crashProcess() {
        // has not to be called here
        throw new UnsupportedOperationException("Not supported in the proxy communication.");
    }
}
