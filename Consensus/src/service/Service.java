/* Coyright Eric Cariou, 2009 - 2011 */

package service;

import communication.CommunicationException;
import communication.Message;
import communication.SynchronizedBuffer;
import service.consensus.IConsensus;

/**
 * Skeleton of a service that has to be specialized for implementing a new service
 */
public abstract class Service {

    /**
     * The message dispatcher to use for associating the current service with its type of message
     */
    protected MessageDispatcher dispatcher;

    /**
     * Communication element to use to send messages to service parts on other processes
     */
    protected ICommunication commElt;

    /**
     * Buffer containing the received messages for the service
     */
    protected SynchronizedBuffer<Message> buffer;

    /**
     * The type of the service
     */
    protected MessageType myType;
    
    protected IConsensus consensusElement;

    /**
     * Send a message, tagged with the type of the service, to a given process (more precisely,
     * to the service of the same type on this process)
     * @param msg the message to send
     */
    protected void sendMessage(Message msg) throws CommunicationException {
        commElt.sendMessage(new TypedMessage(msg.getProcessId(), msg.getData(), myType));
    }

    /**
     * Initialize the service: register the service on the message dispatcher
     * @param dispatcher the message dispatcher to use for associating the current service with its type of message
     * @param commElt the communication element to use to send messages to service parts on other processes
     * @param myType the type of the service
     */
    public void initialize(MessageDispatcher dispatcher, ICommunication commElt, MessageType myType) {
        this.dispatcher = dispatcher;
        this.commElt = commElt;
        this.myType = myType;
        buffer = dispatcher.associateService(myType);
    }
    
    public void initialize(MessageDispatcher dispatcher, IConsensus consensusElement, MessageType myType) {
        this.dispatcher = dispatcher;
        this.consensusElement = consensusElement;
        this.myType = myType;
        buffer = dispatcher.associateService(myType);
    }
}
