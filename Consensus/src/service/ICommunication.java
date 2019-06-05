/* Coyright Eric Cariou, 2009 - 2011 */

package service;

import communication.*;

/**
 * Communication operations for sending and receiving messages. For message received methods, the
 * identifier of the message sender is specified in the received message.
 */
public interface ICommunication {

    /**
     * Send a message to a remote process which address is embedded in the message.
     *
     * @param msg the message to send
     * @throws CommunicationException in case of communication error
     */
    public void sendMessage(Message msg) throws CommunicationException;

    /**
     * Send a message to a remote process.
     *
     * @param id the identifier, including its physical address, of the remote process
     * @param data the data to send to the remote process
     * @throws CommunicationException in case of communication error
     */
    public void sendMessage(ProcessIdentifier id, Object data) throws CommunicationException;

    /**
     * Return a received message coming from any remote process. If no unread message is available
     * wait for the reception of a message.
     *
     * @return the next unread received message
     */
    public Message synchReceiveMessage();

    /**
     * Return a received message coming from any remote process. If no unread message is available
     * return directly the <code>null</code> value.
     *
     * @return the next unread received message or <code>null</code> if none
     */
    public Message asynchReceiveMessage();

    /**
     * Check if there is unread received messages
     *
     * @return <code>true</code> if there is at least one unread received message,
     * <code>false</code> otherwise
     */
    public boolean availableMessage();

   /**
     * Depending of the crash level and a random value generated, crash the process or not
     */
    public void crashProcess();
}
