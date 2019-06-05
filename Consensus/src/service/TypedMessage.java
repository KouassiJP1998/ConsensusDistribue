/* Coyright Eric Cariou, 2009 - 2011 */

package service;

import communication.Message;
import communication.ProcessIdentifier;

/**
 * A typed message is a message with its associated type of service
 */
public class TypedMessage extends Message {

    /**
     * Type of the service
     */
    protected MessageType type;

    /**
     * @return the type of the service
     */
    public MessageType getType() {
        return type;
    }

    /**
     * @param type the type of the service
     */
    public void setType(MessageType type) {
        this.type = type;
    }

    /**
     * @return the message embedded in the typed message
     */
    public Message untypeMessage() {
        return new Message(this.processId, this.data);
    }

    /**
     * Create a typed message
     * @param processId the process identifier for the message
     * @param data data the data of the message
     * @param type the type of the service
     */
    public TypedMessage(ProcessIdentifier processId, Object data, MessageType type) {
        super(processId, data);
        this.type = type;
    }

}
