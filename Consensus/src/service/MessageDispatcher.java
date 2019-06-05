/* Coyright Eric Cariou, 2009 - 2011 */

package service;

import communication.Message;
import communication.SynchronizedBuffer;
import java.util.HashMap;

/**
 *
 */
public class MessageDispatcher {

    /**
     * This hash map associates a buffer to each type of message
     */
    protected HashMap<MessageType, SynchronizedBuffer> serviceMap;

    /**
     * For a given message type, create and associate a dedicated buffer
     * @param type the type of the message
     * @return the buffer associated with this message type
     */
    public SynchronizedBuffer<Message> associateService(MessageType type) {
        SynchronizedBuffer<Message> buffer = new SynchronizedBuffer<Message>();
        serviceMap.put(type, buffer);
        return buffer;
    }

    /**
     * Depending on the type of the message, put this message in the associated buffer
     * @param msg the message to dispatch to the associated buffer
     */
    public void newEvent(TypedMessage msg) {
        SynchronizedBuffer<Message> buffer = serviceMap.get(msg.getType());
        if (buffer==null) {
            System.err.println("[ERROR] No service associated with "+msg.getType()+" messages");
            System.err.println("Unable to deliver : "+msg);
            return;
        }
        buffer.addElement(msg.untypeMessage());
    }

    public MessageDispatcher() {
        serviceMap = new HashMap<MessageType, SynchronizedBuffer>();
    }

}
